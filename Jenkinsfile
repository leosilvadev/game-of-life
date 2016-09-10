node {
   userRemoteConfigs: [[url: 'https://github.com/leosilvadev/game-of-life.git']]])

   stage 'Build'
   setGitHubPullRequestStatus context: 'Building', message: 'Build the whole stuff!', state: 'PENDING'
   sh "chmod u+x gradlew"
   sh "./gradlew clean build"

   stage 'Done'
   setGitHubPullRequestStatus context: 'Done!', message: 'Wow! Everything worked great!', state: 'SUCCESS'
}
