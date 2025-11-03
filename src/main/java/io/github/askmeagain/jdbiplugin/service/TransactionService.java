package io.github.askmeagain.jdbiplugin.service;

import io.github.askmeagain.jdbiplugin.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static io.github.askmeagain.jdbiplugin.SampleDataLoader.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionService {

  private final SampleRepository sampleRepository;

  @Transactional
  public void insertFullBeans() {
    printTxId();
    sampleRepository.insertFullBean(ENTITY_3);
    sampleRepository.insertFullBean(ENTITY_4);
    sampleRepository.insertFullBean(CHILD_1);
    sampleRepository.insertFullBean(CHILD_2);
    printTxId();
    printTxId();
    printTxId();
  }

  void printTxId() {
    log.info("Current TxId: " + sampleRepository.getCurrentTxId());
  }
}
