function [averages] = num_alternating() 

%{
Output format: 
    1. N
    2. number of steps
    3. number (1): odd connections
    4. number (2): shared even - crossed
    5. number (3): shared even - bridge
    6. number (4): unshared - bridge
    7. number (5): shared - no bridge
    8. number (6): unshared - no bridge
    9. longest number of consecutive 5s or 6s
    10. max number of times a node goes from even to odd
    11. max number of times a node is affected by 1
    12. max number of times a node is affected by 2
    13. max number of times a node is affected by 3
    14. max number of times a node is affected by 4
    15. max number of times a node is affected by 5
    16. max number of times a node is affected by 6
%}

M = dlmread('countingSteps3.txt');
[rows, cols] = size(M); 

averages = []; 
maxes = [];

currRow = 1; 
while currRow + 49 <= rows
    averages = [averages; sum(M(currRow : currRow + 49, :)) / 50];
    maxes = [maxes; max(M(currRow : currRow + 49, :))]; 
    currRow = currRow + 50; 
end

remaining = M(currRow : end, :); 

averages = [averages; sum(remaining) / size(remaining, 1)];
maxes = [maxes; max(remaining)];

% averages
figure;
hold on;
plot(averages(:, 1), averages(: , 2 : 8)); 
xlabel('N'); 
ylabel('average steps taken'); 
legend('total', '(1) odd', '(2) crossed', '(3) shared-bridge', ...
    '(4) unshared-bridge', '(5) shared - no bridge', '(6) unshared - no bridge'); 

% max
figure;
hold on;
plot(maxes(:, 1), maxes(: , 2 : 8)); 
xlabel('N'); 
ylabel('max steps taken'); 
legend('total', '(1) odd', '(2) crossed', '(3) shared-bridge', ...
    '(4) unshared-bridge', '(5) shared - no bridge', '(6) unshared - no bridge'); 

% averages
figure;
hold on;
plot(averages(:, 1), averages(: , 7 : 8)); 
xlabel('N'); 
ylabel('average steps taken'); 
legend('(5) shared - no bridge', '(6) unshared - no bridge'); 

% max
figure;
hold on;
plot(maxes(:, 1), maxes(: , 7 : 8)); 
xlabel('N'); 
ylabel('max steps taken'); 
legend('(5) shared - no bridge', '(6) unshared - no bridge'); 

%averages
figure;
hold on;
plot(averages(:, 1), averages(:, 9));
xlabel('N');
ylabel('average consecutive "bad" steps');  

%max
figure;
hold on;
plot(maxes(:, 1), maxes(:, 9));
xlabel('N');
ylabel('max consecutive "bad" steps');

%averages
figure;
hold on; 
plot(averages(:, 1), averages(:, 10)); 
xlabel('N'); 
ylabel('average times a node goes from even -> odd'); 

%max
figure;
hold on;
plot(maxes(:, 1), maxes(:, 10)); 
xlabel('N'); 
ylabel('max times node goes from even -> odd'); 

% averages
figure;
hold on;
plot(averages(:, 1), averages(: , 11 : 16)); 
xlabel('N'); 
ylabel('average times nodes affected by'); 
legend('(1) odd', '(2) crossed', '(3) shared-bridge', ...
    '(4) unshared-bridge', '(5) shared - no bridge', '(6) unshared - no bridge'); 

% max
figure;
hold on;
plot(maxes(:, 1), maxes(: , 11 : 16)); 
xlabel('N'); 
ylabel('max times nodes affected by'); 
legend('(1) odd', '(2) crossed', '(3) shared-bridge', ...
    '(4) unshared-bridge', '(5) shared - no bridge', '(6) unshared - no bridge'); 

% averages
figure;
hold on;
plot(averages(:, 1), averages(: , 15 : 16)); 
xlabel('N'); 
ylabel('average times nodes affected by'); 
legend('(5) shared - no bridge', '(6) unshared - no bridge'); 

% max
figure;
hold on;
plot(maxes(:, 1), maxes(: , 15 : 16)); 
xlabel('N'); 
ylabel('max times nodes affected by'); 
legend('(5) shared - no bridge', '(6) unshared - no bridge');

end

