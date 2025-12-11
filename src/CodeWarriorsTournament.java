import java.util.*;
    public class CodeWarriorsTournament {
        static Scanner scanner = new Scanner(System.in);
        static ArrayList<String> teamNames = new ArrayList<>();
        static ArrayList<double[]> teamScores = new ArrayList<>();

        public static void main(String[] args){
            System.out.println("=== CODEWARRIORS CHAMPIONSHIP 2025 ===\n");
            inputTurnamentData();
            System.out.println("\n=== TOURNAMENT STATISTICS ===\n");
            for(int i = 0; i < teamNames.size(); i++){
                String name = teamNames.get(i);
                double[] scores = teamScores.get(i);
                System.out.println(calculateTeamStats(name, scores));
                double predicted = predictNextScore(scores);
                displayStats(name, scores);
                displayStats(name, scores, predicted);
                System.out.println();
            }
            determineWinners();
            specialAwards();
            scanner.close();
        }

        public static void inputTurnamentData() {
            int numTeams = 0;
            while (true) {
                System.out.print("Enter number of teams (3-10): ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input! Please enter an integer.");
                    scanner.next();
                    continue;
                }
                numTeams = scanner.nextInt();
                scanner.nextLine();
                if (numTeams >= 3 && numTeams <= 10) break;
                System.out.println("Invalid! Must be between 3-10.");
            }

            for (int t = 1; t <= numTeams; t++) {
                System.out.println("\n--- Team " + t + " ---");
                System.out.print("Team name: ");
                String name = scanner.nextLine().trim();
                while (name.isEmpty()) {
                    System.out.print("Name cannot be empty. Team name: ");
                    name = scanner.nextLine().trim();
                }
                teamNames.add(name);

                double[] scores = new double[5];
                System.out.println("Enter 5 match scores (0-100):");
                for (int m = 0; m < 5; m++) {
                    while (true) {
                        System.out.print("Match " + (m + 1) + ": ");
                        if (!scanner.hasNextDouble()) {
                            System.out.println("Invalid! Enter numeric score between 0 and 100.");
                            scanner.next();
                            continue;
                        }
                        double s = scanner.nextDouble();
                        scanner.nextLine();
                        if (s >= 0 && s <= 100) {
                            scores[m] = s;
                            break;
                        } else {
                            System.out.println("Invalid! Score must be between 0 and 100.");
                        }
                    }
                }
                teamScores.add(scores);
            }
        }

        public static String calculateTeamStats(String teamName, double[] scores) {
            double total = 0.0, max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY;
            for (int i = 0; i < scores.length; i++) {
                double s = scores[i];
                total += s;
                if (s > max) max = s;
                if (s < min) min = s;
            }
            double average = total / (double) scores.length;
            double range = max - min;
            String performance = average >= 85 ? "OUTSTANDING" : (average >= 70 ? "EXCELLENT" : (average >= 60 ? "GOOD" : "NEEDS IMPROVEMENT"));
            String consistency = range <= 15 ? "HIGHLY CONSISTENT" : (range <= 30 ? "CONSISTENT" : "INCONSISTENT");
            return String.format("Team: %s\nAverage Score: %.2f\nHighest: %.0f | Lowest: %.0f\nPerformance: %s\nConsistency: %s (Range: %.0f)\n",
                    teamName, average, max, min, performance, consistency, range);
        }

        public static void displayStats(String teamName, double[] scores) {
            double total = 0.0;
            for (double s : scores) total += s;
            double avg = total / (double) scores.length;
            System.out.println(String.format("Summary -> Team: %s | Total: %.0f | Average: %.2f", teamName, total, avg));
        }

        public static void displayStats(String teamName, double[] scores, double predictedNext) {
            System.out.println(String.format("Predicted Next Score: %.2f", predictedNext));
        }

        static class TeamResult {
            String name;
            double total;
            double average;
            double[] scores;
            TeamResult(String name, double total, double average, double[] scores) {
                this.name = name; this.total = total; this.average = average; this.scores = scores;
            }
        }

        public static void determineWinners() {
            ArrayList<TeamResult> results = new ArrayList<>();
            for (int i = 0; i < teamNames.size(); i++) {
                String name = teamNames.get(i);
                double[] scores = teamScores.get(i);
                double total = 0;
                for (double s : scores) total += s;
                double avg = total / (double) scores.length;
                results.add(new TeamResult(name, total, avg, scores));
            }
            Collections.sort(results, new Comparator<TeamResult>() {
                public int compare(TeamResult a, TeamResult b) {
                    if (b.total != a.total) return Double.compare(b.total, a.total);
                    return Double.compare(b.average, a.average);
                }
            });
            System.out.println("=== FINAL RANKINGS ===");
            if (results.size() >= 1) {
                TeamResult champ = results.get(0);
                System.out.println(String.format("🥇 Champion: %s - Total: %.0f - Average: %.2f", champ.name, champ.total, champ.average));
            }
            if (results.size() >= 2) {
                TeamResult second = results.get(1);
                System.out.println(String.format("🥈 Runner Up: %s - Total: %.0f - Average: %.2f", second.name, second.total, second.average));
            }
            if (results.size() >= 3) {
                TeamResult third = results.get(2);
                System.out.println(String.format("🥉 Third Place: %s - Total: %.0f - Average: %.2f", third.name, third.total, third.average));
            }
            System.out.println();
        }

        public static void specialAwards() {
            String mostImproved = "None"; double bestImprovement = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < teamNames.size(); i++) {
                double[] s = teamScores.get(i);
                double improvement = s[s.length - 1] - s[0];
                if (improvement > bestImprovement && improvement > 0) { bestImprovement = improvement; mostImproved = teamNames.get(i); }
            }

            double[] minFirst3 = new double[3]; double[] maxLast2 = new double[2];
            for (int j = 0; j < 3; j++) minFirst3[j] = Double.POSITIVE_INFINITY;
            for (int j = 0; j < 2; j++) maxLast2[j] = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < teamScores.size(); i++) {
                double[] s = teamScores.get(i);
                for (int m = 0; m < 3; m++) if (s[m] < minFirst3[m]) minFirst3[m] = s[m];
                for (int m = 3; m < 5; m++) if (s[m] > maxLast2[m - 3]) maxLast2[m - 3] = s[m];
            }

            String comebackKing = "None";
            for (int i = 0; i < teamNames.size(); i++) {
                double[] s = teamScores.get(i);
                boolean lowestInFirst3 = true; boolean highestInLast2 = true;
                for (int m = 0; m < 3; m++) if (!(s[m] == minFirst3[m])) { lowestInFirst3 = false; break; }
                for (int m = 3; m < 5; m++) if (!(s[m] == maxLast2[m - 3])) { highestInLast2 = false; break; }
                if (lowestInFirst3 && highestInLast2) { comebackKing = teamNames.get(i); break; }
            }

            String consistencyMaster = "None"; double smallestRange = Double.POSITIVE_INFINITY;
            for (int i = 0; i < teamNames.size(); i++) {
                double[] s = teamScores.get(i);
                double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY;
                for (double v : s) { if (v > max) max = v; if (v < min) min = v; }
                double range = max - min;
                if (range < smallestRange) { smallestRange = range; consistencyMaster = teamNames.get(i); }
            }

            System.out.println("=== SPECIAL AWARDS ===");
            if (!mostImproved.equals("None")) System.out.println(String.format("🏆 Most Improved Team: %s (+%.0f points)", mostImproved, bestImprovement));
            else System.out.println("🏆 Most Improved Team: None");
            System.out.println("⚡ Comeback King: " + comebackKing);
            System.out.println(String.format("🎯 Consistency Master: %s (Range: %.0f)", consistencyMaster, smallestRange));
            System.out.println();
        }

        public static double predictNextScore(double[] scores) {
            if (scores == null || scores.length < 2) return scores == null ? 0.0 : scores[scores.length - 1];
            double sumDiff = 0.0;
            for (int i = 1; i < scores.length; i++) sumDiff += (scores[i] - scores[i - 1]);
            double avgChange = sumDiff / (double) (scores.length - 1);
            double predicted = scores[scores.length - 1] + avgChange;
            predicted = predicted < 0 ? 0 : (predicted > 100 ? 100 : predicted);
            predicted = Math.round(predicted * 100.0) / 100.0;
            return predicted;
        }
    }
