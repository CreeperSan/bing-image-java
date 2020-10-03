package com.creepersan.bingimage.entrance

class HelpEntrance: BaseEntrance {

    override fun run(params: Array<String>) {
        println("Thanks for download BingImage!")
        println("Usage:")
        println("    no params      - Run app")
        println("    help           - Show help message")
        println("    config         - Application preferences setting, like location and database connection something else")
        println("    backup         - Backup image and database data into a .bak file in root directory")
        println("    restore        - Restore image and database data from a .bak file in root directory")
    }

}