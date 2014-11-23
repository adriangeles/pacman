private void updateGameBar() {

   Thread update = new Thread(new Runnable() {

      @Override
      public void run() {
         Message msg = new Message();
         activity.handler.sendMessage(msg);
      }
   });
   
   update.start();
}