function [averages] = process_counting() 

M = dlmread('countingSteps.txt');
[rows, cols] = size(M); 

averages = []; 

currRow = 1; 
while currRow + 49 <= rows
    averages = [averages; sum(M(currRow : currRow + 49, :)) / 50];
    currRow = currRow + 50; 
end

remaining = M(currRow : end, :); 

averages = [averages; sum(remaining) / size(remaining, 1)];

figure;
hold on;
plot(averages(:, 1), averages(: , 2 : 8)); 
xlabel('N'); 
ylabel('steps taken'); 
legend('total', '(1) odd', '(2) crossed', '(3) shared-bridge', ...
    '(4) unshared-bridge', '(5) shared - no bridge', '(6) unshared - no bridge'); 

figure;
hold on;
plot(averages(:, 1), averages(: , 7 : 8)); 
xlabel('N'); 
ylabel('steps taken'); 
legend('(5) shared - no bridge', '(6) unshared - no bridge'); 

figure;
hold on;
plot(averages(:, 1), averages(:, 9));
xlabel('N');
ylabel('consecutive "bad" steps');  

end