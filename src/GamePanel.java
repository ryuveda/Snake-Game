import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH=600;
    static final int SCREEN_HEIGHT=600;
    static final int UNIT_SIZE=25;//
    static final int GAME_UNITS=((SCREEN_HEIGHT*SCREEN_WIDTH)/(UNIT_SIZE));
    static final int DELAY=75;
    final int x[]=new int[GAME_UNITS];
    final int y[]=new int[GAME_UNITS];
    int bodyParts=6;
    int prizeEaten;
    int prizeX;
    int prizeY;
    char direction='R';
    boolean running =false;
    Timer timer;
    Random random;

    GamePanel(){
        random=new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame(){
        newPrize();
        running=true;
        timer=new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics grap){
        super.paintComponent(grap);
        draw(grap);
    }
    public void draw(Graphics grap){

        if(running) {
            //Grid Lines
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                grap.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                grap.drawLine(0, i * UNIT_SIZE, SCREEN_HEIGHT, i * UNIT_SIZE);
            }
            */
            //Prize
            grap.setColor(Color.red);
            grap.fillOval(prizeX, prizeY, UNIT_SIZE, UNIT_SIZE);

            //Snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    grap.setColor(new Color(0, 0, 255));
                    grap.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    //grap.setColor(new Color(25, 50, 255));
                    grap.setColor(new Color(random.nextInt( 255), random.nextInt( 255),random.nextInt( 255)));
                    grap.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            //Score Text
            grap.setColor(Color.ORANGE);
            grap.setFont(new Font("Courier", Font.BOLD, 30));
            FontMetrics metrics=getFontMetrics(grap.getFont());
            grap.drawString("Score: "+prizeEaten,(SCREEN_WIDTH-metrics.stringWidth("Score: "+prizeEaten))/2,grap.getFont().getSize());

        }
        else {
            gameOver(grap);
        }
    }
    public void newPrize(){
        prizeX= random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        prizeY= random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){

        for(int i=bodyParts;i>0;i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch (direction)
        {
            case 'U':
                y[0]=y[0]-UNIT_SIZE;
                break;
            case 'D':
                y[0]=y[0]+UNIT_SIZE;
                break;
            case 'L':
                x[0]=x[0]-UNIT_SIZE;
                break;
            case 'R':
                x[0]=x[0]+UNIT_SIZE;
                break;
        }
    }
    public void checkPrize(){
        if (x[0] == prizeX && y[0]==prizeY) {
            bodyParts++;
            prizeEaten++;
            newPrize();
        }

    }
    public void checkCollisions(){
        //checks if head collieds with body
        for (int i=bodyParts;i>0;i--){
            if((x[0]==x[i]) && (y[0]==y[i])){
                running=false;
            }
        }
        //checks if the head touches right or left border
        if((x[0])>SCREEN_WIDTH || x[0]<0)
        {
            running=false;
        }
        //checks if the head touches top or bottom border
        if((y[0])>SCREEN_WIDTH || y[0]<0)
        {
            running=false;
        }
        if(!running){
            timer.stop();
        }
    }
    public void gameOver(Graphics grap){
        //Game Over Text
        grap.setColor(Color.ORANGE);
        grap.setFont(new Font("Courier", Font.BOLD, 80));
        FontMetrics metrics=getFontMetrics(grap.getFont());
        grap.drawString("GAME OVER",(SCREEN_WIDTH-metrics.stringWidth("GAME OVER"))/2,SCREEN_HEIGHT/2);
        //Score Text
        grap.setColor(Color.ORANGE);
        grap.setFont(new Font("Courier", Font.BOLD, 30));
        FontMetrics metricsScore=getFontMetrics(grap.getFont());
        grap.drawString("Score: "+prizeEaten,(SCREEN_WIDTH-metricsScore.stringWidth("Score: "+prizeEaten))/2,grap.getFont().getSize());

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkPrize();
            checkCollisions();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent event){
            switch (event.getKeyCode()){
                case KeyEvent.VK_LEFT :
                    if(direction !='R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction !='L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction !='D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN :
                    if(direction !='U'){
                        direction='D';
                    }
                    break;
            }
        }
    }
}
