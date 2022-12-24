if (redis.call('EXISTS', KEYS[1]) == 1) then
    local stock = tonumber(redis.call('GET', KEYS[1]))
    if (stock > 0) then
        redis.call('INCRBY', KEYS[1], -1)
        return stock
    end
    return 0
end