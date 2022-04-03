package com.berge.ratenow.testapplication.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.berge.ratenow.testapplication.config.exception.BusinessRuleViolatedException;
import com.berge.ratenow.testapplication.config.exception.NotFoundException;
import com.berge.ratenow.testapplication.dto.ChangePasswordCommand;
import com.berge.ratenow.testapplication.dto.EditUserCommand;
import com.berge.ratenow.testapplication.dto.FilterUserCommand;
import com.berge.ratenow.testapplication.dto.ResetPasswordCommand;
import com.berge.ratenow.testapplication.dto.UserCommand;
import com.berge.ratenow.testapplication.dto.UserDataCommand;
import com.berge.ratenow.testapplication.entities.security.Authority;
import com.berge.ratenow.testapplication.entities.security.User;
import com.berge.ratenow.testapplication.repositories.security.AuthorityRepository;
import com.berge.ratenow.testapplication.repositories.security.UserRepository;
import com.berge.ratenow.testapplication.service.UserService;
import com.berge.ratenow.testapplication.utils.PaginacionUtil;
import com.berge.ratenow.testapplication.utils.seguridad.SeguridadUtil;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private MessageSource messageSource;

    @Override
    public User create(UserCommand command) {
        return userRepository.save(
                User.builder()
                        .username(command.getUsername())
                        .password(bCryptPasswordEncoder.encode(command.getPassword()))
                        .name(command.getName())
                        .lastName(command.getLastName())
                        .enabled(command.getEnabled())
                        .rolesAssigned(asociatedRoles(command))
                        .build()
        );
    }

    @Override
    public User update(EditUserCommand command) {
        User usuario = userRepository.findByUsername(command.getUsername());
        if (usuario == null) {
            throw new NotFoundException();
        }
        usuario.setName(command.getName());
        usuario.setLastName(command.getLastName());
        usuario.setRolesAssigned(asociatedRoles(command));
        usuario.setEnabled(command.getEnabled());
        boolean admin = false;
        if (command.getAdmmin() != null) {
            admin = command.getAdmmin();
        }
        usuario.setAdmin(admin);
        if (usuario.isAdmin()) {
            usuario.setRolesAssigned(authorityRepository.findAll()
                    .stream()
                    .map(Authority::getName)
                    .collect(Collectors.joining(",")));
        }

        return userRepository.save(usuario);
    }

    private String asociatedRoles(UserCommand command) {
        String rolesAssigned = "";
        if (command.getRolesAssigned() != null && !command.getRolesAssigned().isEmpty()) {
            List<Authority> authorities = authorityRepository.findAllByIdIn(command.getRolesAssigned());
            rolesAssigned = authorities.stream().map(Authority::getName).collect(Collectors.joining(","));
        }
        return rolesAssigned;
    }
    
    private String asociatedRoles(EditUserCommand command) {
        String rolesAssigned = "";
        if (command.getRolesAssigned() != null && !command.getRolesAssigned().isEmpty()) {
            List<Authority> authorities = authorityRepository.findAllByIdIn(command.getRolesAssigned());
            rolesAssigned = authorities.stream().map(Authority::getName).collect(Collectors.joining(","));
        }
        return rolesAssigned;
    }
    
    private String asociatedRole(FilterUserCommand command) {
        String roleAssigned = "";
        if (command.getRole() != null) {
            Optional<Authority> authorities = authorityRepository.findById(command.getRole());
            roleAssigned = authorities.get().getName();
        }
        return roleAssigned;
    }

    @Override
    public User changeEnable(String username) {
        User usuario = userRepository.findByUsername(username);
        if (usuario == null) {
        	LOGGER.debug("Username :" + username + " not found");
            throw new NotFoundException();
        }
        usuario.setEnabled(!usuario.isEnabled());
        return userRepository.save(usuario);
    }
    
    @Override
    public User changeEnable(Long id) {
    	Optional<User> optional = userRepository.findById(id);
    	if (optional == null || optional.get()==null) {
    		LOGGER.debug("Username with id :" + id + " not found");
    		throw new NotFoundException();
    	}
    	User usuario = optional.get();
        usuario.setEnabled(!usuario.isEnabled());
        return userRepository.save(usuario);
    }
    
    @Override
    @SuppressWarnings("All")
    public List<User> list(FilterUserCommand command) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> r = query.from(User.class);

        Predicate predicate = builder.conjunction();

        if (command.getUsername() != null) {
            predicate = builder.and(predicate, builder.like(builder.lower(r.get("username")), "%" + command.getUsername().toLowerCase() + "%"));
        }
        if (command.getLastName() != null) {
            predicate = builder.and(predicate, builder.like(builder.lower(r.get("lastName")), "%" + command.getLastName().toLowerCase() + "%"));
        }
        if (command.getName() != null) {
            predicate = builder.and(predicate, builder.like(builder.lower(r.get("name")), "%" + command.getName().toLowerCase() + "%"));
        }
        if (command.getRole() != null) {
            predicate = builder.and(predicate, builder.like(builder.lower(r.get("rolesAssigned")), "%" + this.asociatedRole(command).toLowerCase() + "%"));
        }
        if (command.getEnabled() != null) {
            predicate = builder.and(predicate, builder.equal(r.get("enabled"), command.getEnabled()));
        }
       
        query.where(predicate);
        TypedQuery<User> typedQuery = (TypedQuery<User>) PaginacionUtil.readyForQuery(entityManager.createQuery(query),null /*pageable*/);
        List<User> lista = typedQuery.getResultList();

        return lista;
    }

    @Override
    public User changePassword(ChangePasswordCommand command) {
        User usuario = userRepository.findByUsername(SeguridadUtil.autheticatedUserName());
        usuario.setPassword(bCryptPasswordEncoder.encode(command.getNewPassword()));
        userRepository.save(usuario);
        return usuario;
    }
    
    @Override
    public User resetPasswordForUser(ResetPasswordCommand command) {
        User usuario = userRepository.findByUsername(command.getUsername());
        if (usuario == null) {
        	LOGGER.debug("Username :" + command.getUsername() + " not found");
            throw new NotFoundException();
      	}
        usuario.setPassword(bCryptPasswordEncoder.encode(command.getNewPassword()));
        userRepository.save(usuario);
        return usuario;
    }

    @Override
    public UserDataCommand userData(UserDataCommand command) {
        User usuario = userRepository.findByUsername(SeguridadUtil.autheticatedUserName());
        usuario.setName(command.getName());
        usuario.setLastName(command.getLastName());
        userRepository.save(usuario);
        return command;
    }
    
    @Override
    public User getUserData(Long id){
    	Optional<User> optional = userRepository.findById(id);
    	if (optional == null || optional.get()==null) {
    		LOGGER.debug("Username with id :" + id + " not found");
    		throw new NotFoundException();
    	}
        
        return optional.get();
	}

	@Override
	public Boolean existsUser(String userName) {
		return userRepository.existsByUsername(userName);
	}

	@Override
	public Boolean delete(Long id) {
		Optional<User> optional = userRepository.findById(id);
        if (optional == null || optional.get()==null) {
        	LOGGER.debug("Username with id :" + id + " not found");
        	return Boolean.FALSE;
        }
        User usuario = optional.get();

        if (SeguridadUtil.autheticatedUserName().equals(usuario.getUsername())) {
        	throw new BusinessRuleViolatedException("Connected user cannot delete their own User Register and Data.");
        }
        
        userRepository.delete(usuario);
        
        return Boolean.TRUE;
     
	}

	@Override
	public User getUserDataByUsername(String autheticatedUserName) {
		// TODO Auto-generated method stub
		return null;
	}

}
