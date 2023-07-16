
db = connect( 'mongodb://prospring6:prospring6@localhost:27017/musicdb?authSource=admin' );

db.singers.insertMany( [
    {
        firstName: "John",
        lastName: "Mayer",
        birthDate: "1977-10-16"
    } ,
    {
        firstName: "Ben",
        lastName: "Barnes",
        birthDate: "1981-08-20"
    },
    {
        firstName: "John",
        lastName: "Butler",
        birthDate: "1975-04-01"
    }
] );

printjson( db.singers.find( {} ) );