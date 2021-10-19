package createUserTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import configuration.ConfigXML;
import dataAccess.DataAccessHeras2;
import domain.User;
import exceptions.AlreadyTakenEMailException;
import exceptions.NotValidEmailException;
import exceptions.NullParameterException;
import exceptions.UserAlreadyTakenException;
import utility.TestUtilityDataAccess;

class CreateUserDA2Test {
	
	static DataAccessHeras2 sut = new DataAccessHeras2(ConfigXML.getInstance().getDataBaseOpenMode().equals("initialize"));
	static TestUtilityDataAccess testDA = new TestUtilityDataAccess();


	@Test
	@DisplayName("User correctly added")
	void test1() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="unasiheras@ehu.eus";
		User usr=null;
		try {
			usr = sut.createUser(usrname, passw, eMail);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Something went wrong");
		} 
		
		assertEquals(usr, sut.getUserWithUsernamePassword(usrname, passw));
		sut.removeUser(usr);
	}
			
	@Test
	@DisplayName("user already in DB")
	void test2() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="unasiheras@ehu.eus";
		User usr = testDA.createUser(usrname, passw, eMail);
		
		assertThrows(UserAlreadyTakenException.class, () -> sut.createUser(usrname, passw, eMail));
		sut.removeUser(usr);
	}
	
	@Test
	@DisplayName("Empty user")
	void test3() {
		String usrname="";
		String passw="123456789ABC";
		String eMail="unasiheras@ehu.eus";
		
		assertThrows(NullParameterException.class, () -> sut.createUser(usrname, passw, eMail));
	}
	
	@Test
	@DisplayName("Null password")
	void test4() {
		String usrname="Unai";
		String passw="";
		String eMail="unasiheras@ehu.eus";
		
		assertThrows(NullParameterException.class, () -> sut.createUser(usrname, passw, eMail));
	}
	
	@Test
	@DisplayName("Null eMail")
	void test5() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="";
		
		assertThrows(NullParameterException.class, () -> sut.createUser(usrname, passw, eMail));		
	}
	
	@Test
	@DisplayName("eMail alredy in use")
	void test6() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="unasiheras@ehu.eus";
		User usr = testDA.createUser(usrname, passw, eMail);

		assertThrows(AlreadyTakenEMailException.class, () -> sut.createUser("Imanol", passw, eMail));
		sut.removeUser(usr);

	}
	
	@Test
	@DisplayName("not valid eMail")
	void test7() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="h0l4.1im";
		
		assertThrows(NotValidEmailException.class, () -> sut.createUser(usrname, passw, eMail));
	}

}
