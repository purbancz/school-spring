package pl.edu.agh.ki.mwo.persistence;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.edu.agh.ki.mwo.model.School;
import pl.edu.agh.ki.mwo.model.SchoolClass;
import pl.edu.agh.ki.mwo.model.Student;

public class DatabaseConnector {

	protected static DatabaseConnector instance = null;

	public static DatabaseConnector getInstance() {
		if (instance == null) {
			instance = new DatabaseConnector();
		}
		return instance;
	}

	Session session;

	protected DatabaseConnector() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	public void teardown() {
		session.close();
		HibernateUtil.shutdown();
		instance = null;
	}

	public Iterable<School> getSchools() {
		String hql = "FROM School";
		Query query = session.createQuery(hql);
		List schools = query.list();

		return schools;
	}

//	public Iterable<School> getSchoolById(String schoolId) {
//		String hql = "FROM School S WHERE S.id=" + schoolId;
//		Query query = session.createQuery(hql);
//		List school = query.list();
//
//		return school;
//	}

//	public School getSchoolObjectById(String schoolId) {
//		String hql = "FROM School S WHERE S.id=" + schoolId;
//		Query query = session.createQuery(hql);
//		School school = (School) query.uniqueResult();
//
//		return school;
//	}

	public School getSchoolObjById(String schoolId) {
		School school = (School) session.get(School.class, Long.parseLong(schoolId));
		return school;
	}

	public void addSchool(School school) {
		Transaction transaction = session.beginTransaction();
		session.save(school);
		transaction.commit();
//		session.flush();
	}

	public void changeSchool(String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School s : results) {
			session.update(s);
		}
		transaction.commit();
//		session.flush();
	}

	public void deleteSchool(String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (School s : results) {
			session.delete(s);
		}
		transaction.commit();
//		session.flush();
	}

	public Iterable<SchoolClass> getSchoolClasses() {
		String hql = "FROM SchoolClass";
		Query query = session.createQuery(hql);
		List schoolClasses = query.list();

		return schoolClasses;
	}

	public SchoolClass getSchoolClassObjById(String classId) {
		SchoolClass schoolClass = (SchoolClass) session.get(SchoolClass.class, Long.parseLong(classId));
		return schoolClass;
	}

	public void addSchoolClass(SchoolClass schoolClass, String schoolId) {
		String hql = "FROM School S WHERE S.id=" + schoolId;
		Query query = session.createQuery(hql);
		List<School> results = query.list();
		Transaction transaction = session.beginTransaction();
		if (results.size() == 0) {
			session.save(schoolClass);
		} else {
			School school = results.get(0);
			school.addClass(schoolClass);
			session.save(school);
		}
		transaction.commit();
//		session.flush();
	}

	public void updateSchoolClass(String classId, String oldSchoolId, String trueSchoolId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + classId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (SchoolClass s : results) {
			if (oldSchoolId.equals(trueSchoolId) || trueSchoolId.isEmpty()) {
				session.update(s);
			} else {
				School trueSchool = getSchoolObjById(trueSchoolId);
//				School oldSchool = getSchoolObjById(oldSchoolId);
				trueSchool.addClass(s);
//				oldSchool.removeClass(s);
				session.merge(trueSchool);
//				session.update(trueSchool);
//				session.update(oldSchool);
			}
		}
		transaction.commit();
		session.flush();
		session.clear();

	}

	public void deleteSchoolClass(String schoolClassId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + schoolClassId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (SchoolClass s : results) {
			session.delete(s);
		}
		transaction.commit();
//		session.flush();
	}

	public Iterable<Student> getStudents() {
		String hql = "FROM Student";
		Query query = session.createQuery(hql);
		List students = query.list();

		return students;
	}
	
	public Student getStudentObjById(String studentId) {
		Student student = (Student) session.get(Student.class, Long.parseLong(studentId));
		return student;
	}

	public void addStudent(Student student, String classId) {
		String hql = "FROM SchoolClass S WHERE S.id=" + classId;
		Query query = session.createQuery(hql);
		List<SchoolClass> results = query.list();
		Transaction transaction = session.beginTransaction();
		if (results.size() == 0) {
			session.save(student);
		} else {
			SchoolClass schoolClass = results.get(0);
			student.setSchoolClass(schoolClass);
//			schoolClass.addStudent(student);
			session.save(student);
		}
		transaction.commit();
//		session.flush();
	}
	
	public void updateStudent(String studentId, String oldClassId, String trueClassId) {
		String hql = "FROM Student S WHERE S.id=" + studentId;
		Query query = session.createQuery(hql);
		List<Student> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (Student s : results) {
			if (oldClassId.equals(trueClassId)) {
				session.update(s);
			} else {
				SchoolClass trueClass = getSchoolClassObjById(trueClassId);
				s.setSchoolClass(trueClass);
				session.merge(s);
//				trueClass.addStudent(s);
//				session.merge(trueClass);
			}
		}
		transaction.commit();
//		session.flush();
		session.clear();

	}

	public void deleteStudent(String studentId) {
		String hql = "FROM Student S WHERE S.id=" + studentId;
		Query query = session.createQuery(hql);
		List<Student> results = query.list();
		Transaction transaction = session.beginTransaction();
		for (Student s : results) {
			session.delete(s);
		}
		transaction.commit();
//		session.flush();
	}

}
