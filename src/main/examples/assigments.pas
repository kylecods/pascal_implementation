BEGIN
    BEGIN
        five := -1 + 2 - 3 + 4 + 3;
        ratio := 10 / 2;
        fahrenheit := 72;
         centigrade := (fahrenheit - 32)*ratio;

         centigrade := 25;
         fahrenheit := centigrade/ratio + 32;

         centigrade := 25;
         fahrenheit := 32 + centigrade/ratio
    END;
    dze := fahrenheit/(ratio - ratio);
    BEGIN
        number := 2;
        root := number;
        root := (number/root + root)/2;
    END;

    ch := 'x';
    str := 'hello, world'
END.