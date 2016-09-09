node {
   stage 'Checkout'
   setGitHubPullRequestStatus context: 'Starting', message: 'Let\'s go, it\'s time to start our build!', state: 'PENDING'
   git url: 'https://github.com/leosilvadev/game-of-life'

   stage 'Build'
   setGitHubPullRequestStatus context: 'Building', message: 'Build the whole stuff!', state: 'PENDING'
   sh "chmod u+x gradlew"
   sh "./gradlew clean build"

   setGitHubPullRequestStatus context: 'Done!', message: 'Wow! Everything worked great!', state: 'SUCCESS'
}
