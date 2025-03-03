package com.depomanager.deposito.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.depomanager.deposito.models.Deposito;
import com.depomanager.utilidades.repository.IBaseRepository;

@Repository
public interface IDepositoRepository extends JpaRepository<Deposito, Long>, 
                                              IBaseRepository<Deposito, Long> {}