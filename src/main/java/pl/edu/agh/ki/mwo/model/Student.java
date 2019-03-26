package pl.edu.agh.ki.mwo.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="students")
public class Student implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column
	private String name;
	
	@Column
	private String surname;
	
	@Column
	private String pesel;
	
	
//	@ManyToOne
//	@JoinColumn(name = "class_id")
//	private SchoolClass schlclass;
//	
//	public SchoolClass getSchoolClass() {
//		return schlclass;
//	}
//
//	public void setSchoolClass(SchoolClass schoolClass) {
//		this.schlclass = schoolClass;
//	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {
		return name + surname + "(" + pesel + ")";
	}

	public void setId(long id) {
		this.id = id;
	}

	public Student() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPesel() {
		return pesel;
	}

	public void setPesel(String pesel) {
		this.pesel = pesel;
	}

	
}
