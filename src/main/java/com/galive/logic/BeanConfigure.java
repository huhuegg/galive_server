package com.galive.logic;

import com.galive.logic.service.AccountService;
import com.galive.logic.service.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configurable
@EnableScheduling
@ComponentScan("com.galive.logic")
public class BeanConfigure {

    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }


}
