import javax.swing.*;
import javax.swing.border.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;

public class Display {
    private String screen;
    private JFrame frame;
    private Stable stable;
    private Simulation sim;
    private JPanel panel;
    private JPanel footer;
    private JPanel header;

    public Display(){
        this.frame = new JFrame("No Horsin' Around");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.panel = new JPanel();
        this.footer = new JPanel();
        this.header = new JPanel();
        this.screen = "start";
        this.sim = null;
        this.stable = null;
    }

    //used when user clicks to different screen
    public void update(String screen){
        
        //add needed elements for new screen being drawn
        this.screen = screen;
        this.refresh();
        
        
    }

    //Refreshes so UI can update
    public void refresh(){
        //update stable in main 
        this.stable = sim.getUserStable();
        //clear border from previouspage
        this.panel.setBorder(BorderFactory.createEmptyBorder());
        //remove all panels attached to main panel/footer
        this.panel.removeAll();
        this.footer.removeAll();
        this.header.removeAll();
        //redraw the panel and footer
        this.panel.revalidate();
        this.panel.repaint();
        this.footer.revalidate();
        this.footer.repaint();
        this.draw();
    }

//Picks which screen needs to be drawn
   public void draw(){
        switch(this.screen){
            case "start":
                start();
                break;
            case "main":
                mainScreen();
                break;
            case "race":
                race();
                break;
            case "horses":
                horses();
                break;
            case "employees":
                employees();
                break;
            default:
                System.out.println("error: no function for button");
                return;

        }
        
        this.frame.pack();
        this.frame.setSize(1920,1080);
        this.frame.setVisible(true);
        this.frame.getContentPane().add(BorderLayout.NORTH, header);
        this.frame.getContentPane().add(BorderLayout.CENTER, panel);
        this.frame.getContentPane().add(BorderLayout.SOUTH, footer);
        
   }
   //Creates menu bar to navigate between pages
   private void menu(){
        JMenuBar menu = new JMenuBar();
        JButton main = new JButton("Main");
        main.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                update("main");
            }
        });
        JButton race = new JButton("Upcoming Race");
        race.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                update("race");
            }
        });
        JButton horses = new JButton("Horses");
        horses.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                update("horses");
            }
        });
        JButton employees = new JButton("Employees");
        employees.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                update("employees");
            }
        });
        menu.add(main);
        menu.add(race);
        menu.add(horses);
        menu.add(employees);

        JButton next = new JButton("Next Week");
        next.setBackground(Color.GREEN);
        next.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                //check if the game is over
                if (sim.ended()){
                    String message = "";
                    if (gui.stable.getCash() <= 0){
                        message = "Sorry! You went bankrupt!";
                    } else {
                        message = "You've completed the game! You finished with $" + gui.sim.getUserStable().getCash() + "!";
                    }
                    JOptionPane.showMessageDialog(null, message);
                    sim = new Simulation();
                    update("start");
                }

                if (gui.sim.week % 2 == 0){
                    int numRacers = gui.sim.getCurrentRace().getSize();
                    gui.sim.runRaces();
                    String columns [] = {"Place", "Horse", "Prize"};
                    String data[][] = new String[numRacers][3];
                    for (int i = 1; i <= numRacers; i++ ){
                        RiddenHorse racer = gui.sim.getCurrentRace().getFinisher(i);
                        data[i - 1][0] = Integer.toString(i);
                        data[i - 1][1] = racer.getName();
                        data[i - 1][2] = "$" + gui.sim.getCurrentRace().getPrize(i);
                    }

                    JPanel selectPanel = new JPanel();
                    JTable results = new JTable(data, columns);
                    selectPanel.add(new JScrollPane(results));
                    JOptionPane.showConfirmDialog(null, selectPanel, gui.sim.getCurrentRace().getName() + " Results", JOptionPane.OK_CANCEL_OPTION);
                }
                gui.sim.nextWeek();
                //check if the game is over
                if (sim.ended()){
                    String message = "";
                    if (gui.stable.getCash() <= 0){
                        message = "Sorry! You went bankrupt!";
                    } else {
                        message = "You've completed the game! You finished with $" + gui.sim.getUserStable().getCash() + "!";
                    }
                    JOptionPane.showMessageDialog(null, message);
                    update("start");
                }
                gui.refresh();
            }
        });

        //Menu alements to be aligned to right side
        menu.add(Box.createHorizontalGlue());
        menu.add(next);
        this.header.add(menu);
        
   }

   private void start(){
        //this.frame.removeAll();
        panel.setLayout(new GridLayout(2,1));
        JPanel buttons = new JPanel();
        JButton newGame = new JButton("Start Game");
        newGame.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                gui.sim = new Simulation();
                gui.stable = sim.getUserStable();
                update("main");
            }
        });
        buttons.add(newGame);
        JPanel titlePanel = new JPanel();
        JLabel title = new JLabel("<html><h1><center>No Horsin' Around</center></h1></html>");
        titlePanel.add(title);
        panel.setLayout(new GridLayout(2,1));
        panel.add(titlePanel);
        panel.add(buttons);
   }

   private void race(){
        menu();
        Race race = sim.getCurrentRace();
        if (race != null){ 
            panel.setBorder(BorderFactory.createTitledBorder(race.getName()));
            JLabel length = new JLabel("Length: " + race.getLength() + "m"); 
            panel.add(length);
            JPanel racerPanel = new JPanel();
            racerPanel.setBorder(BorderFactory.createTitledBorder("Racers"));
            String racers [][] = new String[race.getSize()][3];
            String columns [] = {"Jockey", "Horse"};
            ArrayList<RiddenHorse> racerArr = race.getRacers();
            for (RiddenHorse racer: racerArr){
                int index = racerArr.indexOf(racer);
                racers[index][0] = racer.getJockName();
                racers[index][1] = racer.getName();
            }
            JTable raceTable = new JTable(racers, columns);

            JScrollPane racePane = new JScrollPane(raceTable);

            racerPanel.add(racePane);
            panel.add(racePane);

            JButton horseSelect = new JButton("Select a Horse and Jockey");
            horseSelect.addActionListener(new ConcreteActionListener(this){
                public void actionPerformed(ActionEvent e){
                    JPanel selectPanel = new JPanel(new GridLayout(4,1));
                String horses [] = new String [gui.stable.horses.size()];
                for (Horse horse: gui.sim.getUserStable().horses){
                    int index = gui.stable.horses.indexOf(horse);
                    horses[index] = horse.getName();
                }
                String jockeys [] = new String [gui.stable.jockeys.size()];
                for (Jockey jockey: gui.sim.getUserStable().jockeys){
                    int index = gui.stable.jockeys.indexOf(jockey);
                    jockeys[index] = jockey.getName();
                }
                JComboBox horseBox = new JComboBox<String>(horses);
                JComboBox jockeyBox = new JComboBox<String>(jockeys);
                selectPanel.add(new JLabel("Choose a horse:"));
                selectPanel.add(horseBox);
                selectPanel.add(new JLabel("Choose a jockey:"));
                selectPanel.add(jockeyBox);
                //THIRD PARTY CODE - These lines of for the remainder of the actionPressed function are adapted from the link below - I used this and the method using a panel to get multiple elements in a dialog box
                //https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog 
                int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Please Select a Horse and Jockey", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    Horse horse = gui.stable.getHorse((String)horseBox.getSelectedItem());
                    Jockey jockey = gui.stable.getJockey((String)jockeyBox.getSelectedItem());
                    sim.getCurrentRace().addRacer(jockey.ride(horse));
               }

               gui.refresh();
                }
            });
            footer.add(horseSelect);
        } else {
            JPanel msgPanel = new JPanel();
            JLabel message = new JLabel("<html><h1><center>No Race This Week</center></h1></html>");
            msgPanel.add(message);
            panel.add(msgPanel);
        }

   }

   private void mainScreen(){
        menu();
        panel.setBorder(BorderFactory.createTitledBorder(this.stable.getName() + " - Week " + this.sim.week));
        //Panel displaying financial info
        TitledBorder title;
        JPanel finPanel = new JPanel(new GridLayout(4,1));
        title = BorderFactory.createTitledBorder("Finances");
        finPanel.setBorder(title);
        finPanel.add(new JLabel("<html><h1>Cash: $" + stable.getCash() + "</h1></html>"));
        finPanel.add(new JLabel("<html><h2>Expenses: -$" + stable.getExpenses()+ "</h2></html>"));
        finPanel.add(new JLabel("<html><h3>Rent: -$" + stable.getRent()+ "</h3></html>"));
        finPanel.add(new JLabel("<html><h3>Salaires: -$" + stable.getSalaries()+ "</h3></html>"));


        //Panel displaying events from this week
        JPanel newsPanel = new JPanel(new GridLayout(0,1));
        title = BorderFactory.createTitledBorder("News This Week");
        newsPanel.setBorder(title);
        int numEvents = this.sim.recorder.getSize();
        if (numEvents == 0){
            newsPanel.add(new JLabel("No news this week."));
        }
        for (int i = 0; i < numEvents; i++){
            newsPanel.add(new JLabel(this.sim.recorder.getEvent(i)));
        }

        //Panel displaying horses you own
        JPanel horsePanel = new JPanel(new GridLayout());
        title = BorderFactory.createTitledBorder("Horses");
        horsePanel.setBorder(title);
        String [][] horseInfo = new String[this.stable.allHorses.size()][4];
        for (Horse horse: this.stable.allHorses){
            int index = this.stable.allHorses.indexOf(horse);
            horseInfo[index][0] = horse.getName();
            horseInfo[index][1] = Integer.toString(horse.getSpeed());
            horseInfo[index][2] = Integer.toString(horse.getEndurance());
            horseInfo[index][3] = Integer.toString(horse.getTrain());
        }
        String column [] = {"Name", "SPD", "END", "TRN"};
        JTable horseTable = new JTable(horseInfo, column);
        JScrollPane tablePane = new JScrollPane(horseTable);
        horsePanel.add(tablePane);
        
        //Panel displaying trainers and joceys you employ
        JPanel empPanel = new JPanel();
        title = BorderFactory.createTitledBorder("Employees");
        empPanel.setBorder(title);
        GridLayout empLayout = new GridLayout(2, 1);
        empPanel.setLayout(empLayout);
        empPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        //Trainers
        JPanel trnPanel = new JPanel(new GridLayout());
        title = BorderFactory.createTitledBorder("Trainers");
        trnPanel.setBorder(title);
        String [][] trnInfo = new String[this.stable.trainers.size()][4];
        for (Trainer trainer: this.stable.trainers){
            int index = this.stable.trainers.indexOf(trainer);
            trnInfo[index][0] = trainer.getName();
            trnInfo[index][1] = Integer.toString(trainer.getSpeed());
            trnInfo[index][2] = Integer.toString(trainer.getEndurance());
            trnInfo[index][3] = Integer.toString(trainer.getTrain());
        }
        String trnColumn [] = {"Name", "SPD", "END", "TRN"};
        JTable trnTable = new JTable(trnInfo, trnColumn);
        JScrollPane trnPane = new JScrollPane(trnTable);
        trnPanel.add(trnPane);
        empPanel.add(trnPanel);
        //Jockeys
        JPanel jckPanel = new JPanel(new GridLayout());
        title = BorderFactory.createTitledBorder("Jockeys");
        jckPanel.setBorder(title);
        String [][] jckInfo = new String[this.stable.allJockeys.size()][4];
        for (Jockey jockey: this.stable.allJockeys){
            int index = this.stable.allJockeys.indexOf(jockey);
            jckInfo[index][0] = jockey.getName();
            jckInfo[index][1] = Integer.toString(jockey.getSpeed());
            jckInfo[index][2] = Integer.toString(jockey.getEndurance());
            jckInfo[index][3] = jockey.getStyle();
        }
        String jckColumn [] = {"Name", "SPD", "END", "Style"};
        JTable jckTable = new JTable(jckInfo, jckColumn);
        JScrollPane jckPane = new JScrollPane(jckTable);
        jckPanel.add(jckPane);
        empPanel.add(jckPanel);

        //Buttons to allow for name editing
        JButton editStable = new JButton("Edit Stable Name");
        JButton editHorses = new JButton("Edit Horse Name");
        JButton editEmployee = new JButton("Edit Employee Name");
        footer.add(editStable);
        footer.add(editHorses);
        footer.add(editEmployee);
        //Button functionality
        editStable.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                JPanel selectPanel = new JPanel(new GridLayout(2,1));
                JTextField newName = new JTextField();
                selectPanel.add(new JLabel("Enter the new name for the stable:"));
                selectPanel.add(newName);

                int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Rename Stable", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    String name = newName.getText();
                    gui.stable.setName(name);
               }

               gui.refresh();
            }
        });
        editHorses.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                JPanel selectPanel = new JPanel(new GridLayout(4,1));
                String horses [] = new String [gui.stable.allHorses.size()];
                for (Horse horse: gui.stable.allHorses){
                    int index = gui.stable.allHorses.indexOf(horse);
                    horses[index] = horse.getName();
                }
                JComboBox horseBox = new JComboBox<String>(horses);
                JTextField newName = new JTextField();
                selectPanel.add(new JLabel("Select the horse whose name you want to change:"));
                selectPanel.add(horseBox);
                selectPanel.add(new JLabel("Enter the new name:"));
                selectPanel.add(newName);

                int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Rename a Horse", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    Horse horse = gui.stable.getHorse((String)horseBox.getSelectedItem());
                    String name= newName.getText();
                    horse.setName(name);
               }

               gui.refresh();
            }
        });
        editEmployee.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                JPanel selectPanel = new JPanel(new GridLayout(4,1));
                int numJocks = gui.stable.allJockeys.size();
                String employees [] = new String [gui.stable.trainers.size() + numJocks];
                for (Jockey jockey: gui.stable.allJockeys){
                    int index = gui.stable.allJockeys.indexOf(jockey);
                    employees[index] = jockey.getName();
                }
                for (Trainer trainer: gui.stable.trainers){
                    int index = gui.stable.trainers.indexOf(trainer) + numJocks;
                    employees[index] = trainer.getName();
                }
                
                    JComboBox empBox = new JComboBox<String>(employees);
                    JTextField newName = new JTextField();
                    selectPanel.add(new JLabel("Choose an employee to fire:"));
                    selectPanel.add(empBox);
                    selectPanel.add(new JLabel("Enter the new name:"));
                    selectPanel.add(newName);


                    int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Please Select an Employee", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    String name = newName.getText();
                    if (empBox.getSelectedIndex() < numJocks){
                        Jockey jockey = gui.stable.getJockey((String)empBox.getSelectedItem());
                        jockey.setName(name);
                    } else {
                        Trainer trainer = gui.stable.getTrainer((String)empBox.getSelectedItem());
                        trainer.setName(name);
                    }        
                } 

               gui.refresh();
            }
        });
        GridLayout layout = new GridLayout(2, 2);
        panel.setLayout(layout);
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        panel.add(finPanel);
        panel.add(newsPanel);
        panel.add(horsePanel);
        panel.add(empPanel);

   } 

   private void horses(){
        menu();
        
        panel.setLayout(new GridLayout(1,1));
        TitledBorder heading = BorderFactory.createTitledBorder("<html><h1><center>Horses</center></h1></html>");
        panel.setBorder(heading);
        String [][] horseInfo = new String[this.stable.allHorses.size()][6];
        for (Horse horse: this.stable.allHorses){
            int index = this.stable.allHorses.indexOf(horse);
            horseInfo[index][0] = horse.getName();
            horseInfo[index][1] = Integer.toString(horse.getSpeed());
            horseInfo[index][2] = Integer.toString(horse.getEndurance());
            horseInfo[index][3] = Integer.toString(horse.getTrain());
            horseInfo[index][4] = horse.getTrainerName();
            if (horse.getHealth()){
                horseInfo[index][5] = "No";
            } else {
                horseInfo[index][5] = "Yes";
            }
        }
        String column [] = {"Name", "SPD", "END", "TRN", "Assigned Trainer", "Injured?"};

        JTable horseTable = new JTable(horseInfo, column);
        JScrollPane tablePane = new JScrollPane(horseTable);
        //Button to assign a trainer to train a horse
        JButton assignTrainerButton = new JButton("Assign Trainers");
        assignTrainerButton.addActionListener(new ConcreteActionListener(this) {
            public void actionPerformed(ActionEvent e){
                System.out.println("Assign button clicked");
                //Populate drop down menus with horses and trainers
                JPanel selectPanel = new JPanel(new GridLayout(4,1));
                String horses [] = new String [gui.stable.allHorses.size()];
                for (Horse horse: gui.stable.allHorses){
                    int index = gui.stable.allHorses.indexOf(horse);
                    horses[index] = horse.getName();
                }
                String trainers [] = new String [gui.stable.trainers.size()];
                for (Trainer trainer: gui.stable.trainers){
                    int index = gui.stable.trainers.indexOf(trainer);
                    trainers[index] = trainer.getName();
                }
                JComboBox horseBox = new JComboBox<String>(horses);
                JComboBox trainBox = new JComboBox<String>(trainers);
                selectPanel.add(new JLabel("Choose a horse:"));
                selectPanel.add(horseBox);
                selectPanel.add(new JLabel("Choose a trainer:"));
                selectPanel.add(trainBox);

                //Display dialog box with menus. Based on 
                int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Please Select a Horse and Trainer", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    Horse horse = gui.stable.getHorse((String)horseBox.getSelectedItem());
                    Trainer trainer = gui.stable.getTrainer((String)trainBox.getSelectedItem());
                    horse.assignTrainer(trainer);
                    System.out.println(horse.trainer.getName());
               }

               gui.refresh();
            }
        });
        JButton buyHorseButton = new JButton("Buy a New Horse");
        buyHorseButton.addActionListener(new ConcreteActionListener(this) {
            public void actionPerformed(ActionEvent e){
                JPanel selectPanel = new JPanel(new GridLayout(2,1));
                String horses [] = new String [3];
                for (Horse horse: gui.sim.availableHorses){
                    int index = gui.sim.availableHorses.indexOf(horse);
                    horses[index] = horse.getName() + " - $" + horse.getValue();
                }
                    JComboBox horseBox = new JComboBox<String>(horses);
                    selectPanel.add(new JLabel("Choose a horse to sell:"));
                    selectPanel.add(horseBox);

                    int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Please Select a Horse", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    Horse horse = gui.sim.getHorse((String)horseBox.getSelectedItem());
                    gui.stable.buy(horse);
                    for (Horse remainingHorse: gui.stable.allHorses){
                        System.out.println(remainingHorse.getName());
                    }
               } 

               gui.refresh();
            }
        });
        JButton sellHorseButton = new JButton("Sell a Horse");
        sellHorseButton.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                JPanel selectPanel = new JPanel(new GridLayout(2,1));
                String horses [] = new String [gui.stable.allHorses.size()];
                for (Horse horse: gui.stable.allHorses){
                    int index = gui.stable.allHorses.indexOf(horse);
                    horses[index] = horse.getName() + " - $" + horse.getValue();
                }
                    JComboBox horseBox = new JComboBox<String>(horses);
                    selectPanel.add(new JLabel("Choose a horse to sell:"));
                    selectPanel.add(horseBox);

                    int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Please Select a Horse", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    Horse horse = gui.stable.getHorse((String)horseBox.getSelectedItem());
                    gui.stable.sell(horse);
                    for (Horse remainingHorse: gui.stable.allHorses){
                        System.out.println(remainingHorse.getName());
                    }
               } 

               gui.refresh();
            }

            
        });
        

        panel.add(tablePane);
        footer.add(assignTrainerButton);
        footer.add(buyHorseButton);
        footer.add(sellHorseButton);
   }

   private void employees(){
        menu();
            
        panel.setLayout(new GridLayout(1,1));
        TitledBorder heading = BorderFactory.createTitledBorder("<html><h1><center>Employees</center></h1></html>");
        panel.setBorder(heading);
        JPanel trnPanel = new JPanel(new GridLayout());
        heading = BorderFactory.createTitledBorder("Trainers");
        trnPanel.setBorder(heading);
        String [][] trnInfo = new String[this.stable.trainers.size()][5];
        for (Trainer trainer: this.stable.trainers){
            int index = this.stable.trainers.indexOf(trainer);
            trnInfo[index][0] = trainer.getName();
            trnInfo[index][1] = Integer.toString(trainer.getSpeed());
            trnInfo[index][2] = Integer.toString(trainer.getEndurance());
            trnInfo[index][3] = Integer.toString(trainer.getTrain());
            trnInfo[index][4] = "$" + Integer.toString(trainer.getSalary());
        }
        String trnColumn [] = {"Name", "SPD", "END", "TRN", "Salary"};

        JTable trnTable = new JTable(trnInfo, trnColumn);
        JScrollPane trnPane = new JScrollPane(trnTable);
        trnPanel.add(trnPane);
        panel.add(trnPanel);
        
        JPanel jckPanel = new JPanel(new GridLayout());
        heading = BorderFactory.createTitledBorder("Jockeys");
        jckPanel.setBorder(heading);
        String [][] jckInfo = new String[this.stable.allJockeys.size()][5];
        for (Jockey jockey: this.stable.allJockeys){
            int index = this.stable.allJockeys.indexOf(jockey);
            jckInfo[index][0] = jockey.getName();
            jckInfo[index][1] = Integer.toString(jockey.getSpeed());
            jckInfo[index][2] = Integer.toString(jockey.getEndurance());
            jckInfo[index][3] = jockey.getStyle();
            jckInfo[index][4] = "$" + Integer.toString(jockey.getSalary());
        }
        String jckColumn [] = {"Name", "SPD", "END", "Style"};

        JTable jckTable = new JTable(jckInfo, jckColumn);
        JScrollPane jckPane = new JScrollPane(jckTable);
        jckPanel.add(jckPane);
        panel.add(jckPanel);

        JButton fireButton = new JButton("Fire Employee");
        fireButton.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                JPanel selectPanel = new JPanel(new GridLayout(2,1));
                int numJocks = gui.stable.allJockeys.size();
                String employees [] = new String [gui.stable.trainers.size() + numJocks];
                for (Jockey jockey: gui.stable.allJockeys){
                    int index = gui.stable.allJockeys.indexOf(jockey);
                    employees[index] = jockey.getName();
                }
                for (Trainer trainer: gui.stable.trainers){
                    int index = gui.stable.trainers.indexOf(trainer) + numJocks;
                    employees[index] = trainer.getName();
                }
                
                    JComboBox empBox = new JComboBox<String>(employees);
                    selectPanel.add(new JLabel("Choose an employee to fire:"));
                    selectPanel.add(empBox);

                    int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Please Select an Employee", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    if (empBox.getSelectedIndex() < numJocks){
                        Jockey jockey = gui.stable.getJockey((String)empBox.getSelectedItem());
                        gui.stable.fire(jockey);
                        System.out.println("Fired " + jockey.getName());
                    } else {
                        Trainer trainer = gui.stable.getTrainer((String)empBox.getSelectedItem());
                        gui.stable.fire(trainer);
                        System.out.println("Fired " + trainer.getName());
                    }
               } 

               gui.refresh();
            }
        });
        JButton hireButton = new JButton("Hire Employee");
        hireButton.addActionListener(new ConcreteActionListener(this){
            public void actionPerformed(ActionEvent e){
                JPanel selectPanel = new JPanel(new GridLayout(2,1));
                String employees [] = new String [6];
                for (Jockey jockey: gui.sim.availableJockeys){
                    int index = gui.sim.availableJockeys.indexOf(jockey);
                    employees[index] = jockey.getName() + " - Jockey - $" + jockey.getSalary() + " per week";
                }
                for (Trainer trainer: gui.sim.availableTrainers){
                    int index = gui.sim.availableTrainers.indexOf(trainer) + 3;
                    employees[index] = trainer.getName() + " - Trainer - $" + trainer.getSalary() + " per week";
                }
                
                    JComboBox empBox = new JComboBox<String>(employees);
                    selectPanel.add(new JLabel("Choose an employee to hire:"));
                    selectPanel.add(empBox);

                    int result = JOptionPane.showConfirmDialog(null, selectPanel, 
               "Please Select an Employee", JOptionPane.OK_CANCEL_OPTION);

               if (result == JOptionPane.OK_OPTION){
                    if (empBox.getSelectedIndex() < 3){
                        Jockey jockey = gui.sim.getJockey((String)empBox.getSelectedItem());
                        gui.stable.hire(jockey);
                        System.out.println("Hired " + jockey.getName());
                    } else {
                        Trainer trainer = gui.sim.getTrainer((String)empBox.getSelectedItem());
                        gui.stable.hire(trainer);
                        System.out.println("Hired " + trainer.getName());
                    }
               } 

               gui.refresh();
            }
        });
        

        footer.add(fireButton);
        footer.add(hireButton);
   }
}

class ConcreteActionListener implements ActionListener{
    protected Display gui;
    ConcreteActionListener(Display gui){
        this.gui = gui;
    }
    public void actionPerformed(ActionEvent e){};
}

