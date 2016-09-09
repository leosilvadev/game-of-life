node {
   stage 'Checkout'
   git branch: "'${env.CHANGE_ID}'", url: 'https://github.com/leosilvadev/game-of-life'
   setGitHubPullRequestStatus context: 'Starting', message: 'Let\'s go, it\'s time to start our build!', state: 'PENDING'

   stage 'Build'
   setGitHubPullRequestStatus context: 'Building', message: 'Build the whole stuff!', state: 'PENDING'
   sh "chmod u+x gradlew"
   sh "./gradlew clean build"

   stage 'Done'
   setGitHubPullRequestStatus context: 'Done!', message: 'Wow! Everything worked great!', state: 'SUCCESS'
}
