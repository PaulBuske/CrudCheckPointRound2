package com.example.CrudCheckpointRound2;

import com.example.CrudCheckpointRound2.model.User;
import com.example.CrudCheckpointRound2.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
class CrudCheckpointRound2ApplicationTests {

	@Autowired
	MockMvc mvc;

	@Autowired
	UserRepository userRepository;


	@Transactional
	@Rollback
	@Test
	void getReturnsAllItemsOfUserInAnIterableList() throws Exception {

		User user = new User();
		user.setId(1);
		user.setEmail("john@example.com");
		user.setPassword("password");

		User user1 = new User();
		user1.setId(2);
		user1.setEmail("beth@example.com");
		user1.setPassword("123456");

		this.userRepository.save(user);
		this.userRepository.save(user1);

		this.mvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email", is("john@example.com")))
				.andExpect(jsonPath("$[1].email", is("beth@example.com")));
	}

	@Transactional
	@Rollback
	@Test
	void postAUserToTheDatabase() throws Exception {
		this.mvc.perform(post("/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								  [
								    {
								      "id": 1,
								      "email": "john@example.com",
								      "password": "password"
								    },
								    {
								      "id": 2,
								      "email" : "eliza@example.com",
								      "password" : "123456"
								    }
								  ]
												
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].email", is("john@example.com")))
				.andExpect(jsonPath("$[1].email", is("eliza@example.com")))
				.andExpect(jsonPath("$[0].password").doesNotExist());

	}

	@Transactional
	@Rollback
	@Test
	void getWithIdPassedAsVariableReturnsSpecificEntryInTheDatabase() throws Exception {

	User user = new User();
		user.setId(1);
		user.setEmail("john@example.com");
		user.setPassword("password");

		this.userRepository.save(user);

		this.mvc.perform(get("/users/1"))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.email", is("john@example.com")))
				.andExpect(jsonPath("$.password").doesNotExist());
}

	@Transactional
	@Rollback
	@Test
	void getWithIdPassedWithAVariableThatDoesNotExistReturnsErrorMessage() throws Exception {

	User user = new User();
		user.setId(1);
		user.setEmail("john@example.com");
		user.setPassword("password");

		this.userRepository.save(user);


		this.mvc.perform(get("/users/2"))
				.andExpect(content().string("2 not found. Please contact your administrator"));
}

	@Transactional
	@Rollback
	@Test
	void deleteWithPathVariableDeletesASpecificRow() throws Exception {
		User user = new User();
		user.setId(1);
		user.setEmail("john@example.com");
		user.setPassword("password");

		User user1 = new User();
		user1.setId(2);
		user1.setEmail("beth@example.com");
		user1.setPassword("123456");

		this.userRepository.save(user);
		this.userRepository.save(user1);

		this.mvc.perform(delete("/users/1"))
				.andExpect(status().isOk())
				.andExpect(content().string("{\"count\":1}"));
	}

	@Transactional
	@Rollback
	@Test
	void postWithURLAuthenticateChecksToSeeIfPasswordsMatch() throws Exception {
		User user = new User();
		user.setId(1);
		user.setEmail("angelica@example.com");
		user.setPassword("1234");

		User user1 = new User();
		user1.setId(2);
		user1.setEmail("beth@example.com");
		user1.setPassword("123456");

		this.userRepository.save(user);
		this.userRepository.save(user1);

		this.mvc.perform(post("/users/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
							{
						      "email": "angelica@example.com",
						      "password": "1234"
						    }
						"""))
				.andExpect(status().isOk())
				.andExpect(content().string("{\"authenticated\":true,\"user\":{\"id\":1,\"email\":" +
						"\"angelica@example.com\"}}"));
	}

	@Transactional
	@Rollback
	@Test
	void postWithURLAuthenticateANDIncorrectPasswordReturnsUnauthenticated() throws Exception {
		User user = new User();
		user.setId(1);
		user.setEmail("angelica@example.com");
		user.setPassword("1234");

		User user1 = new User();
		user1.setId(2);
		user1.setEmail("beth@example.com");
		user1.setPassword("123456");

		this.userRepository.save(user);
		this.userRepository.save(user1);

		this.mvc.perform(post("/users/authenticate")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
							{
						      "email": "angelica@example.com",
						      "password": "wrongpassword"
						    }
						"""))
				.andExpect(status().isOk())
				.andExpect(content().string("{\"authenticated\":false}"));
	}
}
