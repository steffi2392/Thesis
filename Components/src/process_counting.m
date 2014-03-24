function [averages] = process_counting() 

M = dlmread('countChains3.txt');
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
plot(averages(:, 1), averages(: , 2 : 4)); 
xlabel('N'); 
ylabel('number of chains'); 
legend('even', 'odd', 'singletons'); 

figure;
hold on;
plot(averages(:, 1), averages(:, 5));
xlabel('N');
ylabel('number of nodes'); 
legend('max chain length'); 

figure;
hold on;
plot(averages(:, 1), averages(:, 6));
xlabel('N');
ylabel('number of nodes'); 
legend('average chain length'); 

figure;
hold on;
plot(averages(:, 1), averages(:, 7));
xlabel('N');
ylabel('number of nodes'); 
legend('median chain length'); 

end