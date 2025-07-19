def greedycoinchange(coins,amount):
    coins.sort(reverse = True)
    result = []
    for coin in coins:
        while amount >= coin:
            result.append(coin)
            amount -= coin
   
    if amount != 0:
        print("Can not make any change with the coins u gave")
        return []
    
    return result        

coins = [1, 5, 10, 25]
amount = 63
change = greedycoinchange(coins,amount)
print("coins used for change: " , change)