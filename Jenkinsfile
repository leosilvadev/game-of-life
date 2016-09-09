node {
   stage 'Checkout'
   git url: 'https://github.com/leosilvadev/game-of-life'

   stage 'Build'
   sh "chmod u+x gradlew"
   sh "./gradlew clean build"
}
