package com.example.southside.models.data;


import com.example.southside.models.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SkillDao extends CrudRepository<Skill, Integer> {

}
