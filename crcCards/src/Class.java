/**
*	The Class class holds the basic information of a class : className, classTime, enrolledStudents,
*	instructorName, and prerequisites.
*	This class has methods to set prerequisites for itself and create a roster(listStudents) for
*	the unique classes.
*/
public class Class
{
	/**
	 * 	This constructs a new Class
	 *  @param className			The name of the class
	 *  @param classTime			The time of the class
	 *  @param enrolledStudents		The amount of students in the class
	 *  @param instrutorName		The name of the instructor
	 *  @param prerequisites		The required prerequisite classes need for this class
	 */
	public Class(String className, String classTime, String[] enrolledStudents, 
				 String instrutorName, String prereqisites)
	{
		
	}
	/**
	 * This method assigns an Instructor to a class and updates the assigned class list of the Instructor
	 * @param instructorID				The ID of the instructor
	 * @param className					The name of the class
	 * @param listofAssignedClasses		The list of assigned classes to the instructor
	 * @return void
	 */
	public void setInstructortoClass(Instructor instructorID, Class className, Instructor[] listofAssignedClasses)
	{
		
	}
	/**
	 * This method sets the required prerequisites to a class
	 * @param className 		The name of the class that requires a prerequisite
	 * @return void
	 */
	public void setPrerequisites(Class className)
	{
		
	}
	
	/**
	 * This method creates a roster that has times of class, the lecture hall, the instructor, 
	 * and a list of all the students
	 * @param classTime 
	 * @param location
	 * @param instructorName
	 * @param enrolledStudents
	 * @return String []
	 */
	public String[] createRoster(Class classTime, LectureHall location, 
								 Instructor instructorName, Class enrolledStudents)
	{
		
	}
	
}