#!groovy
@Library("Reform")
def rtMaven = Artifactory.newMavenBuild()

properties([
        [$class: 'GithubProjectProperty', displayName: 'BAR API Acceptance Tests', projectUrlStr: 'https://git.reform.hmcts.net/bar/bar-api-acceptance-tests'],
        parameters([
                string(defaultValue: 'latest', description: 'bar-api Docker Version', name: 'barApiDockerVersion'),
                string(defaultValue: 'latest', description: 'bar-database Docker Version', name: 'barDatabaseDockerVersion')
        ])
])

lock('BAR API Acceptance Tests') {
    node {
        try {
            stage('Checkout') {
                deleteDir()
                checkout scm
            }

            try {
                stage('Start Docker Images') {
                    env.BAR_API_DOCKER_VERSION = params.barApiDockerVersion
                    env.BAR_DATABASE_DOCKER_VERSION = params.barDatabaseDockerVersion

                    sh 'docker-compose pull'
                    sh 'docker-compose up -d bar-api'
                    sh 'docker-compose up wait-for-startup'
                }

                stage('Run acceptance tests') {
                    rtMaven.tool = 'apache-maven-3.3.9'
                    rtMaven.run pom: 'pom.xml', goals: 'clean package surefire-report:report -Dspring.profiles.active=docker -Dtest=**/acceptancetests/*Test'

                    publishHTML([
                            allowMissing         : false,
                            alwaysLinkToLastBuild: true,
                            keepAll              : false,
                            reportDir            : 'target/site',
                            reportFiles          : 'surefire-report.html',
                            reportName           : 'Acceptance Test Report'
                    ])
                }
            } finally {
                stage('Stop Docker Images') {
                    sh 'docker-compose down'
                }
            }
        } catch (err) {
            notifyBuildFailure channel: '#cc-payments-tech'
            throw err
        }
    }
}
