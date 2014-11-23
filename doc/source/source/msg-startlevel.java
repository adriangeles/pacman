public Handler handler =  new Handler() {

   public void handleMessage(Message msg) {
          updateLifesAndScore();
   }
};

public void updateLifesAndScore() {

   scoreView.setText(
         "Puntuacion: " + GameContext.getScore());

   int lifes = GameContext.getLifes();

   life1.setVisibility(
         lifes > 0 ? View.VISIBLE : View.GONE);

   life2.setVisibility(
         lifes > 1 ? View.VISIBLE : View.GONE);

   life3.setVisibility(
         lifes > 2 ? View.VISIBLE : View.GONE);
}