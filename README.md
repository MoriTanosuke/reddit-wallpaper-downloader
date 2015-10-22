reddit-wallpaper-downloader
===========================

[![Build Status](https://travis-ci.org/MoriTanosuke/reddit-wallpaper-downloader.svg)](https://travis-ci.org/MoriTanosuke/reddit-wallpaper-downloader)

Simple java application to download images from reddit posts.

Compiling
=========

Make sure you have [Maven][0] installed. Then run the following command in the repository directory:

    mvn clean package

Usage
=====

After compiling, run the application as follows:

    java -jar target\reddit-wallpaper-downloader-jar-with-dependencies.jar C:\Path\To\Your\Picture\Directory https://www.reddit.com/r/EarthPorn

[0]: https://maven.apache.org/
