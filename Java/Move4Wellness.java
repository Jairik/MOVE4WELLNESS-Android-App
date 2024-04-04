/* Main file of the program
* Authors: JJ McCauley & Will Lamuth
* Creation Date: 4/2/24
* Last Update: 4/3/24
* Description:  */

class Move4Wellness {
    public static void main(String[] args) {
        //Creating the Model, which will be the database of the app
        Model model = new Model();

        //Creating the controller, which interacts between the GUI and model
        Controller controller = new Controller(model);
    }
}