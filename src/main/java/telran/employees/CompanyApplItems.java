package telran.employees;

import java.util.*;
import telran.view.*;


public class CompanyApplItems {
	static Company company;
	static HashSet<String> departments;
	public static List<Item> getCompanyItems(Company company,
											 HashSet<String> departments) {
		CompanyApplItems.company = company;
		CompanyApplItems.departments = departments;
		Item[] items = {
				Item.of("add employee", CompanyApplItems::addEmployee)	,
				Item.of("display employee data", CompanyApplItems::getEmployee),
				Item.of("remove employee", CompanyApplItems::removeEmployee),
				Item.of("display department budget", CompanyApplItems::getDepartmentBudget),
				Item.of("display departments", CompanyApplItems::getDepartments),
				Item.of("display managers with most factor", CompanyApplItems::getManagersWithMostFactor),
		};
		return new ArrayList<>(List.of(items));

	}
	static void addEmployee(InputOutput io) {
		Employee empl = readEmployee(io);
		String type = io.readStringOptions("Enter employee type",
				"Wrong Employee Type", new HashSet<>
						(List.of("WageEmployee", "Manager", "SalesPerson")));
		Employee result = switch(type) {
			case "WageEmployee" -> getWageEmployee(empl, io);
			case "Manager" -> getManager(empl, io);
			case "SalesPerson" -> getSalesPerson(empl, io);
			default -> null;
		};
		company.addEmployee(result);
		io.writeLine("Employee has been added");
	}
	private static Employee getSalesPerson(Employee empl, InputOutput io) {
		WageEmployee wageEmployee = (WageEmployee) getWageEmployee(empl, io);
		float percents = io.readNumberRange("Enter percents", "Wrong percents value", 0.5, 2).floatValue();
		long sales = io.readNumberRange("Enter sales", "Wrong sales value", 500, 50000).longValue();
		return new SalesPerson(empl.getId(), empl.getBasicSalary(), empl.getDepartment(),
				wageEmployee.getHours(), wageEmployee.getWage(),
				percents, sales);
	}
	private static Employee getManager(Employee empl, InputOutput io) {

		float factor = io.readNumberRange("Enter factor",
				"Wrong factor value", 1.5, 5).floatValue();
		return new Manager(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), factor );
	}
	private static Employee getWageEmployee(Employee empl, InputOutput io) {

		int hours = io.readNumberRange("Enter working hours",
				"Wrong hours value", 10, 200).intValue();
		int wage = io.readNumberRange("Enter hour wage",
				"Wrong wage value", 100, 1000).intValue();
		return new WageEmployee(empl.getId(), empl.getBasicSalary(), empl.getDepartment(), hours, wage);
	}
	private static Employee readEmployee(InputOutput io) {

		long id = readEmplId(io);
		int basicSalary = io.readNumberRange("Enter basic salary", "Wrong basic salary", 2000, 20000).intValue();
		String department = io.readStringOptions("Enter department " + departments, "Wrong department", departments);
		return new Employee(id, basicSalary, department);
	}
	private static long readEmplId(InputOutput io) {
		return io.readNumberRange("Enter id value", "Wrong id value", 1000, 10000).longValue();
	}
	static void getEmployee(InputOutput io) {
		Employee emp = company.getEmployee(readEmplId(io));
		io.writeLine(emp==null ? "Employee not found":formatEmployee(emp));
	}
	static void removeEmployee(InputOutput io) {
		Employee empRemove = company.removeEmployee(readEmplId(io));
		io.writeLine(empRemove==null ? "Employee not found":"Employee removed "+ empRemove);
	}
	static void getDepartmentBudget(InputOutput io) {
		String department = io.readStringOptions("Enter department " + departments, "Wrong department", departments);
		io.writeLine("Department " + department + " budget: " + company.getDepartmentBudget(department));
	}
	static void getDepartments(InputOutput io) {
		io.writeLine("Departments: "+String.join(", ", company.getDepartments()));
	}
	static void getManagersWithMostFactor(InputOutput io) {
		Manager[] managers = company.getManagersWithMostFactor();
		io.writeLine(managers.length == 0 ? "Company is empty" : "");
		for (Manager m : managers) {
			io.writeLine(formatEmployee(m));
		}
	}
	private static String formatEmployee(Employee emp) {
		List<String> details = new ArrayList<>();
		details.add("Employee ID: " + emp.getId());
		details.add("Basic Salary: " + emp.getBasicSalary());
		details.add("Department: " + emp.getDepartment());

		if (emp instanceof Manager manager) {
            details.add("Factor: " + manager.factor);
		} else if (emp instanceof SalesPerson salesPerson) {
            details.add("Hours: " + salesPerson.getHours());
			details.add("Wage: " + salesPerson.getWage());
			details.add("Percents: " + salesPerson.percent);
			details.add("Sales: " + salesPerson.sales);
		} else if (emp instanceof WageEmployee wageEmployee) {
            details.add("Hours: " + wageEmployee.getHours());
			details.add("Wage per Hour: " + wageEmployee.getWage());
		}

		return String.join(", ", details);
	}


}