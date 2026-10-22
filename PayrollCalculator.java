public class PayrollCalculator {


    public static double calculateAverageRating(
            int quality, int productivity, int attendance, int teamwork) {
        validateScore(quality, "Quality");
        validateScore(productivity, "Productivity");
        validateScore(attendance, "Attendance");
        validateScore(teamwork, "Teamwork");

        return (quality + productivity + attendance + teamwork) / 4.0;
    }


    public static int convertAverageToRating(double averageRating) {
        if (averageRating < 1.0 || averageRating > 5.0) {
            throw new IllegalArgumentException(
                    "Average performance score must be between 1 and 5.");
        }
        return (int) Math.round(averageRating);
    }

    private static void validateScore(int score, String category) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException(
                    category + " score must be between 1 and 5.");
        }
    }

    public static double getTaxRate(double salary) {
        if (salary < 25000) {
            return 0;
        } else if (salary < 50000) {
            return 5;
        } else if (salary < 100000) {
            return 10;
        } else {
            return 15;
        }
    }

    public static double calculateDeduction(
            double basicSalary, int leaveDays) {
        return basicSalary / 30.0 * leaveDays;
    }


    public static double calculateOvertimePay(
            double basicSalary, double overtimeHours) {
        double hourlyRate = basicSalary / 30.0 / 8.0;
        return overtimeHours * hourlyRate * 1.5;
    }


    public static double getBonusRate(int performanceRating) {
        switch (performanceRating) {
            case 1: return 0.0;
            case 2: return 2.0;
            case 3: return 5.0;
            case 4: return 8.0;
            case 5: return 10.0;
            default:
                throw new IllegalArgumentException(
                        "Performance rating must be between 1 and 5.");
        }
    }

    public static double calculatePerformanceBonus(
            double basicSalary, int performanceRating) {
        return basicSalary * getBonusRate(performanceRating) / 100.0;
    }

    public static double calculateTax(double taxableSalary) {
        return taxableSalary * getTaxRate(taxableSalary) / 100.0;
    }

    public static double calculateNetSalary(
            double basicSalary, double deduction) {
        double taxableSalary = basicSalary - deduction;
        double tax = calculateTax(taxableSalary);
        return taxableSalary - tax;
    }
}
