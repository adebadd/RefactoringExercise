/*
 * 
 * This is the definition of the Employee object
 * 
 * */

public class Employee {
	private int employeeId;
	private String pps;
	private String surname;
	private String firstName;
	private char gender;
	private String department;
	private double salary;
	private boolean fullTime;

	// Create Employee with no details	
	public Employee() {
		this.employeeId = 0;
		this.pps = "";
		this.surname = "";
		this.firstName = "";
		this.gender = '\0';
		this.department = "";
		this.salary = 0;
		this.fullTime = false;
	}//end Employee with no details

	// Create Employee with details
	public Employee(int employeeId, String pps, String surname, String firstName, char gender, String department, double salary,
			boolean fullTime) {
		this.employeeId = employeeId;
		this.pps = pps;
		this.surname = surname;
		this.firstName = firstName;
		this.gender = gender;
		this.department = department;
		this.salary = salary;
		this.fullTime = fullTime;
	}// end Employee with details

	public int getEmployeeId() {
		return this.employeeId;
	}

	public String getPps() {
		return pps;
	}

	public String getSurname() {
		return this.surname;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public char getGender() {
		return this.gender;
	}

	public String getDepartment() {
		return this.department;
	}

	public double getSalary() {
		return this.salary;
	}

	public boolean getFullTime() {
		return this.fullTime;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	
	public void setPps(String pps) {
		this.pps = pps;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGender(char gender) {
		this.gender = gender;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public void setFullTime(boolean fullTime) {
		this.fullTime = fullTime;
	}

	public String toString() {
		String bool = "";
		if (fullTime)
			bool = "Yes";
		else
			bool = "No";

		return "Employee ID: " + this.employeeId + "\nPPS Number: " + this.pps + "\nSurname: " + this.surname
				+ "\nFirst Name: " + this.firstName + "\nGender: " + this.gender + "\nDepartment: " + this.department + "\nSalary: " + this.salary
				+ "\nFull Time: " + bool;
	}// end toString

	public static class Builder {
		private int employeeId;
		private String pps;
		private String surname;
		private String firstName;
		private char gender;
		private String department;
		private double salary;
		private boolean fullTime;

		public Builder setEmployeeId(int employeeId) {
			this.employeeId = employeeId;
			return this;
		}

		public Builder setPps(String pps) {
			this.pps = pps;
			return this;
		}

		public Builder setSurname(String surname) {
			this.surname = surname;
			return this;
		}

		public Builder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public Builder setGender(char gender) {
			this.gender = gender;
			return this;
		}

		public Builder setDepartment(String department) {
			this.department = department;
			return this;
		}

		public Builder setSalary(double salary) {
			this.salary = salary;
			return this;
		}

		public Builder setFullTime(boolean fullTime) {
			this.fullTime = fullTime;
			return this;
		}

		public Employee build() {
			return new Employee(this);
		}
	}

	private Employee(Builder builder) {
		this.employeeId = builder.employeeId;
		this.pps = builder.pps;
		this.surname = builder.surname;
		this.firstName = builder.firstName;
		this.gender = builder.gender;
		this.department = builder.department;
		this.salary = builder.salary;
		this.fullTime = builder.fullTime;
	}
}// end class Employee
