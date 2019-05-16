package communication;

import robot.Robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Log {

    private String filName;
    private int conteur=0;
    private Robot robot;
    private LocalDateTime currentTime = LocalDateTime.now();
    private String monNxt;
    private Semaphore sem;

    public Log(Robot robot,Semaphore sem) {
        FileWriter log=null;
        this.sem = sem;
        this.robot = robot;
        this.monNxt = robot.getMonNxt();
        this.filName = "src/log/"+monNxt+"LOG.txt";
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
            System.out.println("ta mere je sui : " + monNxt);
		}
        try {
            log = new FileWriter(filName,false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            currentTime = LocalDateTime.now();
            log.write(currentTime+"@"+monNxt+"@"+robot.getX()+"@"+robot.getY()+"@"+" "+"@"+robot.getOrientation()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sem.release();
    }

    public void ecrire(String message){
        switch(message) {
        case ("d") : 
        	message="poser";
        	break;
        case ("t"):
        	message="prendre";
    		break;
        case ("mouv=non"):
            conteur--;
            break;
    	default :
    		message="mouv=oui";
    		break;
        }
        FileWriter log=null;
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			System.out.println("ta mere je sui : " + monNxt);
		}
        try {
            log = new FileWriter(filName,true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            currentTime = LocalDateTime.now();
            log.write(currentTime+"@"+monNxt+"@"+robot.getX()+"@"+robot.getY()+"@"+message+"@"+robot.getOrientation()+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conteur++;
        sem.release();
    }

    public List<Integer> dernierPosCoop(String nomNXTRobot){
    	List<Integer> result = new LinkedList<>();
        if (nomNXTRobot==null){
            result.add(-1);
            result.add(-1);
            return result;}
        Scanner scanner;
        String current = null;
        String []curSplit;
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
            System.out.println("ta mere je suis : " + monNxt);
		}
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            result.add(-1);
            result.add(-1);
            return result;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
        }
        if (current!=null) {
            curSplit =current.split("@");
            result.add(Integer.parseInt(curSplit[2]));
            result.add(Integer.parseInt(curSplit[3]));
        }
        scanner.close();
        sem.release();
        return result ;
    }

    public void close(){
        FileWriter log=null;
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            log = new FileWriter(filName,true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            log.write(String.valueOf(conteur));
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sem.release();
    }

    public boolean estFiniCoop(String nomNXTRobot){
        if (nomNXTRobot==null){return true;}
        Scanner scanner;
        String current = null;
        String []curSplit;
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
            System.out.println("ta mere je sui : " + monNxt);
		}
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            return false;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
        }

        curSplit = current.split("@");
        sem.release();
        return curSplit.length==1;
    }

    
    public String lastOrientCoop(String nomNXTRobot){
        if (nomNXTRobot==null){return "e";}
        Scanner scanner;
        String current = null;
        String []curSplit = new String[0];
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
        try {
            sem.acquire();
        } catch (InterruptedException e1) {
            System.out.println("ta mere je sui : " + monNxt);
        }
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            return null;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
        }
        if (current!=null) {
            curSplit =current.split("@");
        }
        scanner.close();
        sem.release();
        return curSplit[5];
    }

}