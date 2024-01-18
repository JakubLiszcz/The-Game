import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import static java.nio.charset.StandardCharsets.UTF_8;

public class GraphicalUserInterface{
    Player p1;
    Random rand = new Random();
    JFrame fr = new JFrame("The Game");
    private JLabel label;

    //Panels
    JPanel panelSetPlayer = new JPanel();
    JPanel panelShop = new JPanel();
    JPanel panelGame = new JPanel();
    JPanel panelBattleArea = new JPanel();
    GraphicsPanel graphPanel = new GraphicsPanel();

    //Buttons
    JButton shopButton;
    JButton playButton;
    JButton battleAreaButton;
    JButton dragonAttackButton;
    JButton saveUserDataButton;
    JButton loadUserDataButton;


    private JTextField nameField;
    private JTextArea gameMessageArea;
    private JList<Object> characterList;
    List<String> characters = List.of("Paladin", "Knight", "Barbarian", "Rogue", "Soldier");
    private JList<Weapon> shopList;
    Weapon[] shop = {new Weapon("Normal sword", 10, 80, 2), new Weapon("Dark sword", 30, 40, 3)};

    //Monsters
    Dragon dragon = new Dragon(2);
    Witcher witcher = new Witcher(12);
    Basilisk basilisk = new Basilisk(8);

    //Network connection
    private JTextField messageField;
    private PrintWriter writer;
    private JTextArea messageReceivedArea;
    private Reader reader;
    private BufferedReader readerBuf;


    public void doGUI() {
        doNet();

        p1 = new Player();

        label = new JLabel("Welcome!!!");

        nameField = new JTextField("Set player name",15);

        gameMessageArea = new JTextArea(20,30);
        gameMessageArea.setEditable(false);
        gameMessageArea.setWrapStyleWord(true);
        gameMessageArea.setLineWrap(true);
        JScrollPane scrollGameMessageArea = new JScrollPane(gameMessageArea);
        scrollGameMessageArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollGameMessageArea.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        //STREAM
        List<String> charactersStream = characters.stream().
                sorted((s1,s2) -> s1.compareToIgnoreCase(s2)).
                limit(5).
                toList();
        //

        characterList = new JList<>(charactersStream.toArray());
        shopList = new JList<>(shop);



        panelSetPlayer.setBackground(Color.lightGray);

        panelGame.setLayout(new BoxLayout(panelGame, BoxLayout.Y_AXIS));

        panelShop.setBackground(Color.gray);
        panelShop.setLayout(new BoxLayout(panelShop, BoxLayout.Y_AXIS));

        panelBattleArea.setBackground(Color.LIGHT_GRAY);
        panelBattleArea.setLayout(new BoxLayout(panelBattleArea, BoxLayout.Y_AXIS));



        playButton = new JButton("Play");
        playButton.addActionListener(new PlayButtonListener());

        shopButton = new JButton("Shop");
        shopButton.addActionListener(new ShopButtonListener());

        battleAreaButton = new JButton("Battle area");
        battleAreaButton.addActionListener(new BattleAreaButtonListener());

        dragonAttackButton = new JButton("Attack dragon");
        dragonAttackButton.addActionListener(new DragonAttackButtonListener());

        saveUserDataButton = new JButton("Save");
        saveUserDataButton.addActionListener(e -> saveUserData());

        loadUserDataButton = new JButton("Load");
        loadUserDataButton.addActionListener(new LoadUserDataButtonListener());


        nameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                nameField.setText("");
            }
        });


        characterList.addListSelectionListener(new CharacterSelectionListener());
        shopList.addListSelectionListener(new ShopSelectionListener());



        panelSetPlayer.add(loadUserDataButton);
        panelSetPlayer.add(nameField);
        panelSetPlayer.add(characterList);
        panelSetPlayer.add(playButton);

        panelGame.add(scrollGameMessageArea);
        panelGame.add(battleAreaButton);
        panelGame.add(shopButton);
        panelGame.add(saveUserDataButton);

        panelBattleArea.add(dragonAttackButton);

        panelShop.add(shopList);



        fr.getContentPane().add(BorderLayout.CENTER, panelSetPlayer);
        fr.getContentPane().add(BorderLayout.NORTH, label);


        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setSize(700, 700);
        fr.setVisible(true);

    }


    class CharacterSelectionListener implements ListSelectionListener{ //set player character class
        public void valueChanged(ListSelectionEvent ev){
            if(!ev.getValueIsAdjusting()){
                String option = (String) characterList.getSelectedValue();
                p1.setClass(option);
            }
        }
    }

    class PlayButtonListener implements ActionListener{ //save player data and move to game screen
        public void actionPerformed(ActionEvent ev){
            fr.remove(panelSetPlayer);
            fr.repaint();
            fr.getContentPane().add(BorderLayout.CENTER, graphPanel);
            fr.getContentPane().add(BorderLayout.EAST, panelGame);
            fr.revalidate();

            p1.setName(nameField.getText());
            gameMessageArea.append("New player has just been born!!!!" + "\n");
            gameMessageArea.append("Your name is: " + p1.getName() + "\n");
            gameMessageArea.append("Character class is " + p1.getClassP() + "\n");
        }
    }

    class ShopButtonListener implements ActionListener{ //move to shop
        public void actionPerformed(ActionEvent ev){
            fr.remove(graphPanel);
            fr.remove(panelBattleArea);
            fr.repaint();
            fr.add(panelShop);
            fr.revalidate();

            label.setText("Welcome in shop!");
        }
    }

    class BattleAreaButtonListener implements ActionListener{ //move to battle area
        public void actionPerformed(ActionEvent ev){
            fr.remove(graphPanel);
            fr.remove(panelShop);
            fr.repaint();
            fr.add(panelBattleArea);
            fr.revalidate();

            label.setText("Welcome in battle area!");
        }
    }

    class LoadUserDataButtonListener implements ActionListener{ //load saved player data
        public void actionPerformed(ActionEvent ev){
            loadUserData();

            fr.remove(panelSetPlayer);
            fr.repaint();
            fr.getContentPane().add(BorderLayout.CENTER, graphPanel);
            fr.getContentPane().add(BorderLayout.EAST, panelGame);
            fr.revalidate();

            gameMessageArea.append("New player has just been born!!!!" + "\n");
            gameMessageArea.append("Your name is: " + p1.getName() + "\n");
            gameMessageArea.append("Character class is " + p1.getClassP() + "\n");
        }
    }

    class DragonAttackButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent ev){
            gameMessageArea.append("Attack dragon!!!" +"\n");
            dragon.shout();

            p1.setHealth(p1.getHealth() - dragon.getDamage());
            gameMessageArea.append("Player health: " + p1.getHealth() + "\n");
            dragon.setMonsterHealth(dragon.getMonsterHealth() - p1.getAttack());
            gameMessageArea.append("Monster health: " + dragon.getMonsterHealth() + "\n");
            try{TimeUnit.SECONDS.sleep(2);} catch(InterruptedException e){Thread.currentThread().interrupt();}

            if (dragon.getMonsterHealth() <= 0){
                panelBattleArea.remove(dragonAttackButton);
                fr.repaint();
                p1.setExp(110 + rand.nextInt(150));
                Monster.numberOfMonsters--;
                gameMessageArea.append("Player lvl is: " + p1.getLvl() + "\n");
                gameMessageArea.append("Remaining number of monsters is: " + Monster.numberOfMonsters + "\n");
            }

        }
    }


    class ShopSelectionListener implements ListSelectionListener{
        public void valueChanged(ListSelectionEvent ev){
            if(!ev.getValueIsAdjusting()) {

                if (p1.getMoney() > shopList.getSelectedValue().getWeaponCost()) {
                    p1.setEquipment(shopList.getSelectedValue());
                    p1.setMoney(p1.getMoney() - shopList.getSelectedValue().getWeaponCost());

                    gameMessageArea.append("Your weapon is: " + shopList.getSelectedValue() + "\n");
                    gameMessageArea.append("Attack: " + p1.getAttack() + "\n");
                    gameMessageArea.append("Defense" + p1.getDefense() + "\n");
                    gameMessageArea.append("Money left: " + p1.getMoney() + "\n");
                }
            }
            else {gameMessageArea.append("You dont have enough money" + "\n");}
        }
    }



    static class GraphicsPanel extends JPanel { //class needed to display image
        public void paintComponent (Graphics g){
            Image img = new ImageIcon("GAME.jpg").getImage();
            g.drawImage(img,1, 1, this);
        }
    }


    //SAVE AND LOAD
    private void saveUserData() {
        try {
            FileOutputStream fos = new FileOutputStream("SaveGame.game"); //connection stream
            ObjectOutputStream save = new ObjectOutputStream(fos); //chain stream
            save.writeObject(p1); //serialization of object, class Player must implements Serializable interface
            save.close(); //stream close
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadUserData() {
        try {
            FileInputStream fis = new FileInputStream("SaveGame.game");
            ObjectInputStream read = new ObjectInputStream(fis);
            Player p1Read = (Player) read.readObject();
            p1 = p1Read;
            read.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //


    //NETWORK CONNECTION (CHAT) THROUGH CHANNELS
    public void doNet() {
        configCommunication();

        messageReceivedArea = new JTextArea(15, 30);
        messageReceivedArea.setLineWrap(true);
        messageReceivedArea.setWrapStyleWord(true);
        messageReceivedArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(messageReceivedArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        messageField = new JTextField("Your message...");
        messageField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                messageField.setText("");
            }
        });

        JButton sendButton = new JButton("Send message");
        sendButton.addActionListener(e -> sendMessage());

        panelGame.add(scroll);
        panelGame.add(messageField);
        panelGame.add(sendButton);


        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new messageRecipient());

    }


    private void configCommunication(){

        try  {
            //1 Set connection
            InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 5000); //"127.0.0.1" its adress IP of localHost, this is usefull when we want to test client and server on the same computer / ("server IP adress", portu TCP number)
            SocketChannel channelSC = SocketChannel.open(serverAddress);

            //2 Data reading by BufferedReader stream
            reader = Channels.newReader(channelSC, UTF_8);
            readerBuf = new BufferedReader(reader);

            //3 Data storage by PrintWriter stream
            writer = new PrintWriter(Channels.newWriter(channelSC, UTF_8));
            System.out.println("You just set up communications!!! \n Client adress is: " + channelSC.getLocalAddress());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void sendMessage(){
        writer.println(messageField.getText());
        writer.flush();
        messageField.setText("Your message...");
        messageField.requestFocus();
    }


    public class messageRecipient implements Runnable{
        public void run(){
            String message;
            try{
                while((message = readerBuf.readLine()) != null){
                    messageReceivedArea.append(p1.getName() + ": " + message + "\n");
                }
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}