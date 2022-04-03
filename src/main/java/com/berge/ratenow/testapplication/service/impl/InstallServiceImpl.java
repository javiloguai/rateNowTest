package com.berge.ratenow.testapplication.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.berge.ratenow.testapplication.constants.ApplicationConstants;
import com.berge.ratenow.testapplication.entities.ApplicationConfigEntity;
import com.berge.ratenow.testapplication.entities.security.Authority;
import com.berge.ratenow.testapplication.entities.security.User;
import com.berge.ratenow.testapplication.repositories.ApplicationConfigDaoRepository;
import com.berge.ratenow.testapplication.repositories.security.AuthorityRepository;
import com.berge.ratenow.testapplication.repositories.security.UserRepository;
import com.berge.ratenow.testapplication.service.InstallService;

@Service
@Transactional
public class InstallServiceImpl implements InstallService {
	
	private static final Logger LOGGER = LogManager.getLogger(InstallServiceImpl.class);
	
    private final static String ADMIN = "ADMIN";
    private final static String OPERATOR = "OPERATOR";
    private final static String CONSULTANT_TYPE1 = "CONSULTANT_TYPE1";
    private final static String CONSULTANT_TYPE2 = "CONSULTANT_TYPE2";

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private AuthorityRepository authorityRepository;
    
    @Autowired
    private ApplicationConfigDaoRepository applicationConfigRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void install() {
        createRoles();
        createAdminUser();
        createSomeOtherDefault();
    }

    private void createRoles() {
        if (authorityRepository.count() == 0) {
            authorityRepository.save(new Authority(null, ADMIN));
            authorityRepository.save(new Authority(null, OPERATOR));
            authorityRepository.save(new Authority(null, CONSULTANT_TYPE1));
            authorityRepository.save(new Authority(null, CONSULTANT_TYPE2));
        }
    }

    private void createAdminUser() {
        if (usuarioRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(bCryptPasswordEncoder.encode("1234567890"));
            admin.setName("System");
            admin.setLastName("Administrator");
            admin.setAdmin(true);
            admin.setEnabled(true);
//            String perms = String.join(",", new String[]{ADMIN, CONSULTOR});
//            admin.setRolesAssigned(perms);
            admin.setRolesAssigned(ADMIN+","+OPERATOR+","+CONSULTANT_TYPE1+","+CONSULTANT_TYPE2);
            LOGGER.debug("Creating Admin User >> " + admin.toString());

            usuarioRepository.save(admin);

            admin = new User();
            admin.setUsername("test");
            admin.setPassword(bCryptPasswordEncoder.encode("1234567890"));
            admin.setName("Test");
            admin.setLastName("user");
            admin.setAdmin(false);
            admin.setEnabled(true);
            admin.setRolesAssigned(CONSULTANT_TYPE1+","+CONSULTANT_TYPE2);
            LOGGER.debug("Creating test User >> " + admin.toString());
            
            usuarioRepository.save(admin);
        }
                
    }
    
    private void createSomeOtherDefault() {
        if (!applicationConfigRepository.existsByKeyId(ApplicationConstants.SOME_DATA_KEY)) {       
        	LOGGER.debug("Creating Some default data on database >> " + ApplicationConstants.SOME_DATA_DEFAULT);
            applicationConfigRepository.save(new ApplicationConfigEntity(null,ApplicationConstants.SOME_DATA_KEY,ApplicationConstants.SOME_DATA_DESC,String.valueOf(ApplicationConstants.SOME_DATA_DEFAULT)));

        }
    }
    
  

}
