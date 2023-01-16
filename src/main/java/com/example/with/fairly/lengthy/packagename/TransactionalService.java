package com.example.with.fairly.lengthy.packagename;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
class TransactionalService {

  private final MyTableRepository myTableRepository;

  TransactionalService(MyTableRepository myTableRepository) {
    this.myTableRepository = myTableRepository;
  }

  @Transactional
  public Mono<MyTableEntity> transactionalMethod() {
    return myTableRepository.save(new MyTableEntity(1));
  }
}
