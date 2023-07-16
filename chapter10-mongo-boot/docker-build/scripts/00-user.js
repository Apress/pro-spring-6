db = connect( 'mongodb://root:mainpass@localhost:27017/admin' );

db.createUser(
    {
        user: 'prospring6',
        pwd: 'prospring6',
        roles: [
            {
                role: 'dbOwner',
                db: 'musicdb'
            } ]
} );

printjson( db.getUsers() );