/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.gestionscolaire.initDatas;

import com.example.gestionscolaire.Users.entity.ERole;
import com.example.gestionscolaire.Users.entity.RoleUser;
import com.example.gestionscolaire.Users.repository.IRoleUserRepo;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 *
 * @author Casimir
 */

@Component
@AllArgsConstructor
@Order(1)
public class InitRoles implements ApplicationRunner{

    private IRoleUserRepo roleRepository;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {;
        System.out.println("initialisation des roles");        
        RoleUser superadmin = new RoleUser(ERole.ROLE_SUPERADMIN);
        RoleUser user = new RoleUser(ERole.ROLE_USER);

        if (!roleRepository.existsByName(ERole.ROLE_SUPERADMIN)) {
            roleRepository.save(superadmin);
        }
        if (!roleRepository.existsByName(ERole.ROLE_USER)) {
            roleRepository.save(user);
        }
               
    }
    
}
