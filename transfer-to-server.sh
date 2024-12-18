#!/bin/bash

stopping_existing_container_app() {
#     ssh homesrv_user docker ps -q -f name=jcode-test_app_1
#     if [ $? -ne 0 ];
    CHECK="`ssh homesrv_user docker ps -q -f name=jcode-test_app`"
    if [ -n "$CHECK" ];
    then
        echo "Остановка приложения..."
        ssh homesrv_user docker stop $(ssh homesrv_user docker ps -q -f name=jcode-test_app_1)
    fi
}

removing_existing_app_image() {
#     ssh homesrv_user docker images jcode-test_app -q
#     if [ $? -ne 0 ];
    CHECK="`ssh homesrv_user docker images jcode-test_app -q`"
    if [ -n "$CHECK" ];
    then
        echo "Удаление образа контейнера приложения..."
        ssh homesrv_user docker rmi $(ssh homesrv_user docker images jcode-test_app -q)
    fi
}

removing_remote_folder() {
    if ssh homesrv_user '[ -d /home/developer/Documents/Workspace/java-projects/jcode-test ]';
    then
        echo "Удаление папки с сервера..."
        ssh homesrv_user rm -r /home/developer/Documents/Workspace/java-projects/jcode-test
    fi
}

removing_remote_archive() {
    if ssh homesrv_user '[ -e /home/developer/Documents/Workspace/java-projects/jcode-test-sources.tgz ]';
    then
        echo "Удаление архива с сервера..."
        ssh homesrv_user rm /home/developer/Documents/Workspace/java-projects/jcode-test-sources.tgz
    fi
}

removing_local_archive() {
    if [ -e "/home/dimidrol/Documents/Workspace/git/jcode-test-sources.tgz" ];
    then
        echo "Удаление локального архива..."
        rm /home/dimidrol/Documents/Workspace/git/jcode-test-sources.tgz
    fi
}

creating_local_archive() {
    echo "Создание локального архива..."
    tar -czf jcode-test-sources.tgz jcode-test
}

transfering_archive_to_server() {
    echo "Передача архива на сервер..."
    scp jcode-test-sources.tgz homesrv_user:/home/developer/Documents/Workspace/java-projects
}

unpacking_archive_on_remote_server() {
    echo "Распаковка архива на сервере..."
    ssh homesrv_user tar -xf /home/developer/Documents/Workspace/java-projects/jcode-test-sources.tgz -C /home/developer/Documents/Workspace/java-projects
}

stopping_all_running_containers() {
#     ssh homesrv_user docker ps -q -f status=running
#     if [ $? -ne 0 ];
    CHECK="`ssh homesrv_user docker ps -q -f status=running`"
    if [ -n "$CHECK" ];
    then
        echo "Остановка всех запущенных контейнеров..."
        ssh homesrv_user docker stop $(ssh homesrv_user docker ps -q -f status=running)
    fi
}

cleaning_exited_docker_containers_on_remote_server() {
#     ssh homesrv_user docker ps -a -q -f status=exited
#     if [ $? -ne 0 ];
    CHECK="`ssh homesrv_user docker ps -a -q -f status=exited`"
    if [ -n "$CHECK" ];
    then
        echo "Очистка остановленных контейнеров на сервере..."
        ssh homesrv_user docker rm $(ssh homesrv_user docker ps -a -q -f status=exited)
    fi
}

create_new_compose_app_from_sources() {
    if ssh homesrv_user '[ -e /home/developer/Documents/Workspace/java-projects/jcode-test/docker-compose.yaml ]';
    then
        echo "Создание нового контейнера из compose файла..."
        ssh homesrv_user docker-compose -f /home/developer/Documents/Workspace/java-projects/jcode-test/docker-compose.yaml up
    fi
}

cd ..
#local
removing_remote_folder
removing_remote_archive

#remote
removing_local_archive
creating_local_archive

#transfer
transfering_archive_to_server
unpacking_archive_on_remote_server

#remote docker
#stopping_all_running_containers
stopping_existing_container_app
cleaning_exited_docker_containers_on_remote_server
removing_existing_app_image
create_new_compose_app_from_sources

