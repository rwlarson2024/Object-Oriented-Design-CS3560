/**
* The Student Class holds all the basic properties of a student : studentName, studentID, and listofGrades
* This class has methods to addClasses, deleteClasses, createSchedule, and createReportCard.
*/
public class Students
{
	/**
	 * This constructs a new Student
	 * @param studentName			The name of the student
	 * @param studentID				The ID of a unique student
	 * @param listofGrades			A list of all grades for the classes a student has
	 * @param listofAddedClasses 	A list of classes added to a student's schedule
	 */
	public Students(String studentName, String studentID, String[] listofGrades,
			String[] listofAddedClasses)
	{
		
	}
	/**
	 * This method adds a class to listofAddedClasses 
	 * @param className
	 * @param listofAddedClasses
	 * @return void
	 */
	public void addClass(Class className, Students[] listofAddedClasses)
	{
		
	}
	/**
	 * This method removes a class to listofAddedClasses 
	 * @param className
	 * @param listofAddedClasses
	 * @return void
	 */
	public void dropClass(Class className, Students[] listofAddedClasses)
	{
		
	}
	/**
	 * This method creates a formated schedule of all the classes in the listofAddedClasses
	 * @param listofAddedClasses
	 * @return String[]
	 */
	public String[] createSchedule(Students[] listofAddedClasses)
	{
		
	}
	/**
	 * This method creates a formated report card of all the assigned grades that a student has
	 * in their listofAddedClasses
	 * @param listofGrades
	 * @return String[]
	 */
	public String[] createReportCard(Students[] listofGrades)
	{
		
	}
}