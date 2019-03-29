package pl.edu.agh.ki.mwo.web.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.edu.agh.ki.mwo.model.Student;
import pl.edu.agh.ki.mwo.persistence.DatabaseConnector;

@Controller
public class StudentsController {
	
	@RequestMapping(value="/Students")
    public String listStudents(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	
        return "studentsList";    
    }
	
	@RequestMapping(value="/AddStudent")
    public String displayAddStudentForm(Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";

    	model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
    	model.addAttribute("schools", DatabaseConnector.getInstance().getSchools());
       	
        return "studentForm";    
    }
	
	@RequestMapping(value="/CreateStudent", method=RequestMethod.POST)
    public String createStudent(@RequestParam(value="studentName", required=false) String name,
    		@RequestParam(value="studentSurname", required=false) String surname,
    		@RequestParam(value="studentPesel", required=false) String pesel,
    		@RequestParam(value="studentSchoolClass", required=false) String classId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	Student student = new Student();
    	student.setName(name);
    	student.setSurname(surname);
    	student.setPesel(pesel);
    	    	
    	DatabaseConnector.getInstance().addStudent(student, classId);
    	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Student został dodany");
         	
    	return "studentsList";
    }
	
	@RequestMapping(value = "/ModifyStudent")
	public String displayChangeStudentForm(@RequestParam(value="studentId", required=false) String studentId,
			Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";
		model.addAttribute("editStudent", DatabaseConnector.getInstance().getStudentObjById(studentId));
		model.addAttribute("schoolClasses", DatabaseConnector.getInstance().getSchoolClasses());
		return "studentChangeForm";
	}

	@RequestMapping(value = "/UpdateStudent", method = RequestMethod.POST)
	public String updateSchoolClass(@RequestParam(value = "studentId", required = false) String studentId,
			@RequestParam(value = "oldClass", required = false) String oldClassId,
			@RequestParam(value = "trueClass", required = false) String trueClassId,
			@RequestParam(value="studentName", required=false) String studentName,
			@RequestParam(value="studentSurname", required=false) String studentSurname,
    		@RequestParam(value="studentPesel", required=false) String studentPesel,
    		Model model, HttpSession session) {
		if (session.getAttribute("userLogin") == null)
			return "redirect:/Login";
		
		Student student = DatabaseConnector.getInstance().getStudentObjById(studentId);
		student.setName(studentName);
		student.setSurname(studentSurname);
		student.setPesel(studentPesel);
		
		DatabaseConnector.getInstance().updateStudent(studentId, oldClassId, trueClassId);
		model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
		model.addAttribute("message", "Dane zostały zmodyfikowane");

		return "studentsList";
	}
	
	@RequestMapping(value="/DeleteStudent", method=RequestMethod.POST)
    public String deleteStudent(@RequestParam(value="studentId", required=false) String studentId,
    		Model model, HttpSession session) {    	
    	if (session.getAttribute("userLogin") == null)
    		return "redirect:/Login";
    	
    	DatabaseConnector.getInstance().deleteStudent(studentId);    	
       	model.addAttribute("students", DatabaseConnector.getInstance().getStudents());
    	model.addAttribute("message", "Student został usunięty");
         	
    	return "studentsList";
    }

}
