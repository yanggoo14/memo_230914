package com.memo.user.reposity;

import org.springframework.data.jpa.repository.JpaRepository;

import com.memo.user.entity.UserEntity;

	public interface UserRepository extends JpaRepository<UserEntity, Integer> {
		
		// null or UserEntity(단건)
		public UserEntity findByLoginId(String loginId);
		
		public UserEntity findByLoginIdAndPassword(String loginId,String password);
}
