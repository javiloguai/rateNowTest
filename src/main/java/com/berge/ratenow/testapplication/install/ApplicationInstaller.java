package com.berge.ratenow.testapplication.install;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.berge.ratenow.testapplication.service.InstallService;

/**
 * Realizar la instalacion del sistema
 * <p>
 * El sistema se instalara cuando detecte que la tabla usuarios se encuentre vacia. En
 * ese momento, creara el primer usuairo, con las opciones de menu definidas y los roles
 * especidicados
 */
//@Data
/**
 * @author jruizh
 *
 */
@Component
public class ApplicationInstaller {

    @Autowired
    private InstallService installService;

    @EventListener(ApplicationReadyEvent.class)
    public void prepareData() {
        installService.install();
        
    }
}
