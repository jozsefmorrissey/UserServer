package com.userSrvc.server.repo;


import org.springframework.transaction.annotation.Transactional;

import com.userSrvc.client.entities.App;
import com.userSrvc.client.repo.AppBaseRepository;

@Transactional
public interface AppRepo extends AppBaseRepository<App> {}

