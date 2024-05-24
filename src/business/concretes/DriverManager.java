package business.concretes;

import business.abstracts.DriverService;
import business.abstracts.UserService;
import entities.Driver;
import entities.HowTaxi;
import entities.Pay;

import java.util.Collections;
import java.util.Scanner;

public class DriverManager implements DriverService, UserService {
    private CustomerManager customerManager;
    Driver driver = new Driver("Driver", "+994938328", 0.0, 0.0);

    public DriverManager(CustomerManager customerManager) {
        this.customerManager = customerManager;
    }

    private static final double BASE_FARE = 2.0;
    private static final double RATE_PER_KM = 1.0;
    private static final double AVERAGE_SPEED_KM_PER_HOUR = 30.0;
    double fee;
    String formattedValue;

    private void calculateArrivalTime() {
        Collections.sort(customerManager.getDifferences());
        double distance = customerManager.getDifferences().get(0);
        double time = (distance / AVERAGE_SPEED_KM_PER_HOUR) * 60;
        String formattedValue = String.format("%.0f", time);
        System.out.println(formattedValue + " deqiqeye orada olacaq.");
    }

    private double taxi(HowTaxi howTaxi) {
        Collections.sort(customerManager.getDifferences());
        double difference = customerManager.getDifferences().get(0);
        double rateMultiplier;
        switch (howTaxi) {
            case ECO -> rateMultiplier = 1.0;
            case COMFORT -> rateMultiplier = 1.5;
            case XL -> rateMultiplier = 2.0;
            case SEND -> rateMultiplier = 1.2;
            case TRAVEL -> rateMultiplier = 2.5;
            default -> throw new IllegalArgumentException("Unknown taxi type: " + howTaxi);
        }
        return BASE_FARE + (RATE_PER_KM * rateMultiplier * difference);
    }

    private void calculateFare() {
        System.out.println("Nece taxi isdeyirsiniz?");
        System.out.println("""
                1->Eco
                2->Comfort
                3->6 neferlik
                4->Send
                5->Travel
                Secin:  """);
        Scanner scanner = new Scanner(System.in);
        int sec = scanner.nextInt();
        HowTaxi selectedTaxiType;
        switch (sec) {
            case 1 -> selectedTaxiType = HowTaxi.ECO;
            case 2 -> selectedTaxiType = HowTaxi.COMFORT;
            case 3 -> selectedTaxiType = HowTaxi.XL;
            case 4 -> selectedTaxiType = HowTaxi.SEND;
            case 5 -> selectedTaxiType = HowTaxi.TRAVEL;
            default -> {
                System.out.println("Yanlis secim.");
                return;
            }
        }

        fee = taxi(selectedTaxiType);
        formattedValue = String.format("%.2f", fee);

        if (reject(fee)) {
            System.out.println("Gedis haqqi: " + formattedValue);
            System.out.println("""
                    Hansi ile odenilecek?
                    1->Nagd
                    2->Kart
                    Secin:""");
            int paymentChoice = scanner.nextInt();
            Pay pay;
            if (paymentChoice == 1) {
                pay = Pay.CASH;
            } else if (paymentChoice == 2) {
                pay = Pay.CREDI_CARD;
            } else {
                System.out.println("Yanlis secim.");
                return;
            }
            processPayment(pay);
        } else {
            System.out.println(driver.name() + " sizi qebul etmedi!");
        }
    }

    private void processPayment(Pay pay) {
        if (pay == Pay.CASH) {
            System.out.println("Nagd ile odenilecek.");
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Kard nomresini daxil edin:");
            String cardNumber = scanner.nextLine();
            if (!cardNumber.isEmpty()) {
                System.out.println("Odenildi!");
            } else {
                System.out.println("Kard nomresini daxil edin!");
            }
        }
    }

    @Override
    public void accept() {
        Collections.sort(customerManager.getDifferences());
        double difference = customerManager.getDifferences().get(0);
        String formattedValue = String.format("%.3f", difference);

        System.out.print(driver.name() + " " + formattedValue + " km uzaqliqdadir. ");
        calculateFare();
        calculateArrivalTime();

    }

    private boolean reject(double fee) {
        return fee > 5;
    }

    @Override
    public void register() throws IllegalAccessException {
    }

    @Override
    public void login() throws IllegalAccessException {
    }
}
