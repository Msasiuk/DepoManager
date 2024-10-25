package com.depomanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depomanager.model.Deposito;

@Repository
public interface IDepositoRepository extends JpaRepository<Deposito, Long>, 
                                              IBaseRepository<Deposito, Long> {}