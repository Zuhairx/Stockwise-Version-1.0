@echo off
echo Starting StockWise Inventory Management System...
java --module-path target\lib --add-modules javafx.controls,javafx.fxml -jar target\stockwise-1.0-SNAPSHOT-shaded.jar
pause
