package com.phongvan.bomberman.AI;

import com.phongvan.bomberman.entities.mobs.Bomber;
import com.phongvan.bomberman.map.MapHandler;

import java.awt.font.MultipleMaster;

public class BomberAI {
    private Bomber player;
    private Data data;
    private Task plan;

    public BomberAI(Bomber player) {
        this.player = player;
    }

    public void initAI() {
        data = new Data(player);
        createPlan();
    }

    private void createPlan() {
        plan = new Selector(data, "Bomber's plan for 1 update");
        //plan = new ResetDecorator(data, plan, "Auto reset");

        Task checkSafety = new Sequence(data, "Check safety of bomber and make decision");
        ((ParentTaskController) checkSafety.getController()).addTask(new CheckBomberSafetyTask(data, "Check safety"));

        Task safetyTasks = new Selector(data, "Some tasks after safety check");
        ((ParentTaskController) checkSafety.getController()).addTask(safetyTasks);

        Task getTheItems = new Sequence(data, "Go and get the item");
        ((ParentTaskController) getTheItems.getController()).addTask(new CheckItemExistTask(data, "Check if the item exists"));
        ((ParentTaskController) getTheItems.getController()).addTask(new GoToTargetPositionTask(data, "Go to the item position"));
        ((ParentTaskController) safetyTasks.getController()).addTask(getTheItems);

        Task escapeThroughPortal = new Sequence(data, "Find and escape through the portal");
        ((ParentTaskController) escapeThroughPortal.getController()).addTask(new CheckAllEnemyDied(data, "Check if all enemy have died"));
        Task findPortal = new Selector(data, "Find the portal");
        Task portalExist = new Sequence(data, "Tasks when the portal exists");
        ((ParentTaskController) portalExist.getController()).addTask(new CheckPortalExistTask(data, "Check if portal exists"));
        ((ParentTaskController) portalExist.getController()).addTask(new GoToTargetPositionTask(data, "Go to the portal position"));
        ((ParentTaskController) findPortal.getController()).addTask(portalExist);
        Task plantBombToFindPortal = new Sequence(data, "Go and find the portal");
        ((ParentTaskController) plantBombToFindPortal.getController()).addTask(new CalculatePlantPositionForBrickTask(data, "Calculate best position for destroying bricks"));
        ((ParentTaskController) plantBombToFindPortal.getController()).addTask(new GoToTargetPositionTask(data, "Go to the best position"));
        Task plantTheBomb = new Sequence(data, "Plant the bomb after find the best position");
        ((ParentTaskController) plantTheBomb.getController()).addTask(new CheckHaveBombToPlantTask(data, "Check if have bomb"));
        ((ParentTaskController) plantTheBomb.getController()).addTask(new PlantTheBombTask(data, "Plant bomb"));
        ((ParentTaskController) plantBombToFindPortal.getController()).addTask(plantTheBomb);
        ((ParentTaskController) findPortal.getController()).addTask(plantBombToFindPortal);
        ((ParentTaskController) escapeThroughPortal.getController()).addTask(findPortal);
        ((ParentTaskController) safetyTasks.getController()).addTask(escapeThroughPortal);

        Task goAndKillEnemy = new Sequence(data, "Go and kill enemy");
        //((ParentTaskController) goAndKillEnemy.getController()).addTask(new CheckIfBomberReachEnemyTask(data, "Check if bomber can reach enemy"));
        ((ParentTaskController) goAndKillEnemy.getController()).addTask(new CalculatePlantPositionToKillEnemyTask(data, "Calculate plant position to kill enemy"));
        ((ParentTaskController) goAndKillEnemy.getController()).addTask(new GoToTargetPositionTask(data, "Go to plant position"));
        Task plantTheBombToKill = new Sequence(data, "Plant the bomb");
        ((ParentTaskController) plantTheBombToKill.getController()).addTask(new CheckHaveBombToPlantTask(data, "Check if have bomb"));
        ((ParentTaskController) plantTheBombToKill.getController()).addTask(new PlantTheBombTask(data, "Plant bomb"));
        ((ParentTaskController) goAndKillEnemy.getController()).addTask(plantTheBombToKill);
        ((ParentTaskController) safetyTasks.getController()).addTask(goAndKillEnemy);

        Task goAndDestroyBrick = new Sequence(data, "Go and destroy bricks");
        ((ParentTaskController) goAndDestroyBrick.getController()).addTask(new CalculatePlantPositionForBrickTask(data, "Calculate plant position to destroy bricks"));
        ((ParentTaskController) goAndDestroyBrick.getController()).addTask(new GoToTargetPositionTask(data, "Go to plant position"));
        Task plantTheBombToDestroy = new Sequence(data, "Plant the bomb");
        ((ParentTaskController) plantTheBombToDestroy.getController()).addTask(new CheckHaveBombToPlantTask(data, "Check if have bomb"));
        ((ParentTaskController) plantTheBombToDestroy.getController()).addTask(new PlantTheBombTask(data, "Plant bomb"));
        ((ParentTaskController) goAndDestroyBrick.getController()).addTask(plantTheBombToDestroy);
        ((ParentTaskController) safetyTasks.getController()).addTask(goAndDestroyBrick);

        Task goAndHideAtSafePosition = new Sequence(data, "Go and hide at safe position");
        ((ParentTaskController) goAndHideAtSafePosition.getController()).addTask(new FindBestSafePositionTask(data, "Find best position to hide"));
        ((ParentTaskController) goAndHideAtSafePosition.getController()).addTask(new GoToTargetPositionTask(data, "Go to the hide position"));

        ((ParentTaskController) plan.getController()).addTask(checkSafety);
        ((ParentTaskController) plan.getController()).addTask(goAndHideAtSafePosition);
    }

    public void start() {
        if (player.isAlive()) {
            plan.getController().start();
        }
    }

    public void update() {
        if (player.isAlive()) {
            //plan.execute();
            int tileSize = MapHandler.getInstance().getTileSize();
            if (player.getX() % tileSize == 0 && player.getY() % tileSize == 0) {
                plan.getController().reset();
                while (!plan.getController().finished()) {
                    System.out.println(player.getX() + " " + player.getY());
                    plan.execute();
                }
            } else {
                player.move(true);
            }
        }
    }
}
