import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

interface Taxable {
    double TAX_RATE = 0.15;
    double calculateTax();
}

interface BonusCalculable {
    double calculateBonus(double performanceScore);
}

abstract class Employee implements Taxable, BonusCalculable, Comparable<Employee> {
    private String id;
    private String name;
    protected double baseSalary;
    private LocalDate joinDate;

    public Employee(String id, String name, double baseSalary, LocalDate joinDate) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary;
        this.joinDate = joinDate;
    }

    public abstract double calculateNetSalary();

    public abstract double calculateBonus(double performanceScore);

    public double calculateTax() {
        return calculateNetSalary() * TAX_RATE;
    }

    public int compareTo(Employee other) {
        return Double.compare(other.calculateNetSalary(), this.calculateNetSalary());
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBaseSalary() { return baseSalary; }
    public LocalDate getJoinDate() { return joinDate; }

    public String toString() {
        return id + " | " + name + " | Net Salary: " + calculateNetSalary()
                + " | Join Date: " + joinDate;
    }
}

class FullTimeEmployee extends Employee {
    private double allowance;

    public FullTimeEmployee(String id, String name, double baseSalary,
                            LocalDate joinDate, double allowance) {
        super(id, name, baseSalary, joinDate);
        this.allowance = allowance;
    }

    public double calculateNetSalary() {
        return baseSalary + allowance;
    }

    public double calculateBonus(double performanceScore) {
        return baseSalary * 0.10 * performanceScore;
    }
}

class ContractorEmployee extends Employee {
    private int hourlyRate;
    private int hoursWorked;

    public ContractorEmployee(String id, String name, LocalDate joinDate,
                              int hourlyRate, int hoursWorked) {
        super(id, name, 0, joinDate);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    public double calculateNetSalary() {
        return hourlyRate * hoursWorked;
    }

    public double calculateBonus(double performanceScore) {
        if (performanceScore > 4.0) {
            return 500;
        }
        return 0;
    }
}

public class PayrollSystem {
    public static void main(String[] args) {

        ArrayList<Employee> list = new ArrayList<>();
        list.add(new FullTimeEmployee("E1", "Khaled", 2800, LocalDate.of(2016, 4, 12), 450));
        list.add(new FullTimeEmployee("E2", "Mariam", 3500, LocalDate.of(2021, 9, 1), 700));
        list.add(new ContractorEmployee("C1", "Yousef", LocalDate.of(2019, 2, 18), 25, 140));
        list.add(new ContractorEmployee("C2", "Hana", LocalDate.of(2023, 6, 5), 40, 90));

        Collections.sort(list);
        System.out.println("--- Sorted by Net Salary (high to low) ---");
        for (Employee e : list) {
            System.out.println(e);
        }

        Collections.sort(list, new Comparator<Employee>() {
            public int compare(Employee a, Employee b) {
                return a.getJoinDate().compareTo(b.getJoinDate());
            }
        });
        System.out.println("--- Sorted by Join Date (oldest first) ---");
        for (Employee e : list) {
            System.out.println(e);
        }

        double score = 4.5;
        double totalTax = 0;
        double totalBonus = 0;
        for (Employee e : list) {
            totalTax = totalTax + e.calculateTax();
            totalBonus = totalBonus + e.calculateBonus(score);
        }
        System.out.println("--- Totals ---");
        System.out.println("Total Tax: " + totalTax);
        System.out.println("Total Bonus: " + totalBonus);
    }
}
