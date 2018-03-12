package org.clc.kernel.repository;

import org.clc.kernel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository extends JpaRepository<User,Integer>{

    User findByName(String name);

    List<User> findByIdIsInAndDeleteFlagIs(Integer[] id,Integer deleteFlag);
}
/*
 Keyword	    Sample	                    JPQL snippet
 -----------------------------------------------------------------------------------------------------------
 IsNotNull	    findByAgeNotNull	        where x.age not null
 Like	        findByNameLike	            where x.name like ?1
 NotLike	    findByNameNotLike	        where x.name not like ?1
 StartingWith	findByNameStartingWith	    where x.name like ?1(parameter bound with appended %)
 EndingWith	    findByNameEndingWith	    where x.name like ?1(parameter bound with prepended %)
 Containing	    findByNameContaining	    where x.name like ?1(parameter bound wrapped in %)
 OrderBy	    findByAgeOrderByName	    where x.age = ?1 order by x.name desc
 Not	        findByNameNot	            where x.name <> ?1
 In	            findByAgeIn                 where x.age in ?1
 NotIn	        findByAgeNotIn	            where x.age not in ?1
 True	        findByActiveTrue	        where x.avtive = true
 False	        findByActiveFalse	        where x.active = false
 And 	        findByNameAndAge	        where x.name = ?1 and x.age = ?2
 Or	            findByNameOrAge	            where x.name = ?1 or x.age = ?2
 Between	    findBtAgeBetween	        where x.age between ?1 and ?2
 LessThan	    findByAgeLessThan	        where x.age  <  ?1
 GreaterThan	findByAgeGreaterThan	    where x.age > ?1
 After/Before	...	...
 IsNull	        findByAgeIsNull	            where x.age is null
 */
