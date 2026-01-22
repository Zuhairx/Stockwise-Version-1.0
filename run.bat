@echo off
echo Redeploying StockWise Inventory Management System...
mvn clean package
if %errorlevel% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)
echo Starting StockWise Inventory Management System...
mvn javafx:run
pause
