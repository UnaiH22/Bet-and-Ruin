package dataAccess;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Bet;
import domain.Event;
import domain.Question;
import domain.User;
import enums.QuestionTypes;
import exceptions.AlreadyTakenEMailException;
import exceptions.NotValidEmailException;
import exceptions.NullParameterException;
import exceptions.QuestionAlreadyExist;
import exceptions.UserAlreadyTakenException;

/**
 * Implements the Data Access utility to the objectDb database
 */
public class DataAccessHeras2  {

	protected EntityManager db;
	protected EntityManagerFactory emf;

	ConfigXML config = ConfigXML.getInstance();

	public DataAccessHeras2(boolean initializeMode)  {
		System.out.println("Creating DataAccess instance => isDatabaseLocal: " + 
				config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());
		open(initializeMode);
	}

	public DataAccessHeras2()  {	
		this(false);
	}


	/**
	 * This method initializes the database with some trial events and questions. 
	 * It is invoked by the business logic when the option "initialize" is used 
	 * in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	public void initializeDB(){

		db.getTransaction().begin();

		try {

			Calendar today = Calendar.getInstance();

			int month = today.get(Calendar.MONTH);
			int aux = month + 1;
			if(today.get(Calendar.DAY_OF_MONTH) > 17) {
				month++;
			}
			int year = today.get(Calendar.YEAR);
			if (month == 12) { month = 0; year += 1;}  

			Event ev1 = new Event(1, "Atlético-Athletic", UtilDate.newDate(year, month, 25));
			Event ev2 = new Event(2, "Eibar-Barcelona", UtilDate.newDate(year, month, 17));
			Event ev3 = new Event(3, "Getafe-Celta", UtilDate.newDate(year, month, 17));
			Event ev4 = new Event(4, "Alavés-Deportivo", UtilDate.newDate(year, month, 17));
			Event ev5 = new Event(5, "Español-Villareal", UtilDate.newDate(year, month, 17));
			Event ev6 = new Event(6, "Las Palmas-Sevilla", UtilDate.newDate(year, month, 17));
			Event ev7 = new Event(7, "Malaga-Valencia", UtilDate.newDate(year, month, 17));
			Event ev8 = new Event(8, "Girona-Leganés", UtilDate.newDate(year, month, 17));
			Event ev9 = new Event(9, "Real Sociedad-Levante", UtilDate.newDate(year, month, 17));
			Event ev10 = new Event(10, "Betis-Real Madrid", UtilDate.newDate(year, month, 17));

			Event ev11 = new Event(11, "Atletico-Athletic", UtilDate.newDate(year, aux, 1));
			Event ev12 = new Event(12, "Eibar-Barcelona", UtilDate.newDate(year, aux, 1));
			Event ev13 = new Event(13, "Getafe-Celta", UtilDate.newDate(year, aux, 1));
			Event ev14 = new Event(14, "Alavés-Deportivo", UtilDate.newDate(year, aux, 1));
			Event ev15 = new Event(15, "Español-Villareal", UtilDate.newDate(year, aux, 1));
			Event ev16 = new Event(16, "Las Palmas-Sevilla", UtilDate.newDate(year, aux, 1));


			Event ev17 = new Event(17, "Málaga-Valencia", UtilDate.newDate(year, month + 1, 28));
			Event ev18 = new Event(18, "Girona-Leganés", UtilDate.newDate(year, month + 1, 28));
			Event ev19 = new Event(19, "Real Sociedad-Levante", UtilDate.newDate(year, month + 1, 28));
			Event ev20 = new Event(20, "Betis-Real Madrid", UtilDate.newDate(year, month + 1, 28));

			db.persist(ev1);
			db.persist(ev2);
			db.persist(ev3);
			db.persist(ev4);
			db.persist(ev5);
			db.persist(ev6);
			db.persist(ev7);
			db.persist(ev8);
			db.persist(ev9);
			db.persist(ev10);
			db.persist(ev11);
			db.persist(ev12);
			db.persist(ev13);
			db.persist(ev14);
			db.persist(ev15);
			db.persist(ev16);
			db.persist(ev17);
			db.persist(ev18);
			db.persist(ev19);
			db.persist(ev20);			

			db.getTransaction().commit();
			System.out.println("The database has been initialized");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param username 
	 * @param password
	 * @param eMail
	 * @return el usuario que ha sido creado
	 * @throws NullParameterException hay campos que no han sido rellenados
	 * @throws UserAlreadyTakenException el usuario ya esta en la base de datos
	 * @throws NotValidEmailException el formato del email no es valido
	 * @throws AlreadyTakenEMailException el email ya esta en uso
	 */
	public User createUser(String username, String password, String eMail) throws NullParameterException, UserAlreadyTakenException, NotValidEmailException, AlreadyTakenEMailException {

		if (username.isEmpty()||password.isEmpty()||eMail.isEmpty()) throw new NullParameterException();
		if (getUserWithUsernamePassword(username, password)!=null) throw new UserAlreadyTakenException();
		if (!checkEmail(eMail)) throw new NotValidEmailException();
		if (getUserWithEMail(eMail)!=null) throw new AlreadyTakenEMailException();
	
		User nU = new User(username, password, eMail);
		db.getTransaction().begin();
		db.persist(nU);
		db.getTransaction().commit();

		return nU;
	}
	
	public boolean removeUser(User u) {
        System.out.println(">> DataAccessTest: removeUser");
        User us = this.getUserWithUsernamePassword(u.getUsername(), u.getPassword());
        if (us!=null) {
            db.getTransaction().begin();
            db.remove(us);
            db.getTransaction().commit();
            return true;
        } else 
        return false;
    }
	
	private boolean checkEmail(String eMail){
		boolean ret = false;
		if(eMail.contains("@")) {
			String[] split = eMail.split("@");
			if(split.length == 2 && split[0].length() > 1 && split[1].contains(".")) {
				ret = true;
			}
		}
		return ret;
	}

	public User getUserWithUsernamePassword(String username, String password){
		User ret;
		List<User> checkList = db.createQuery("SELECT u FROM User u WHERE u.username = \"" + username + "\" and u.password = \"" + password + "\"", User.class).getResultList();
		try {
			ret = checkList.get(0);
		}
		catch(Exception e) {
			ret = null;
		}
		return ret;
	}
	
	public User getUserWithEMail(String eMail) {
		User ret;
		List<User> checkList = db.createQuery("SELECT u FROM User u WHERE u.eMail = \"" + eMail + "\"", User.class).getResultList();
		try {
			ret = checkList.get(0);
		}
		catch (Exception e) {
			ret = null;
		}
		return ret;
	}
	
	public void open(boolean initializeMode){

		System.out.println("Opening DataAccess instance => isDatabaseLocal: " + 
				config.isDataAccessLocal() + " getDatabBaseOpenMode: " + config.getDataBaseOpenMode());

		String fileName = config.getDataBaseFilename();
		if (initializeMode) {
			fileName = fileName + ";drop";
			System.out.println("Deleting the DataBase");
		}

		if (config.isDataAccessLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:" + fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", config.getDataBaseUser());
			properties.put("javax.persistence.jdbc.password", config.getDataBasePassword());

			emf = Persistence.createEntityManagerFactory("objectdb://" + config.getDataAccessNode() +
					":"+config.getDataAccessPort() + "/" + fileName, properties);

			db = emf.createEntityManager();
		}
	}

	public void close(){
		db.close();
		System.out.println("DataBase is closed");
	}
	
}