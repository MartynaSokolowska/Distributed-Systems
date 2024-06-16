#ifndef LIGHT_ICE
#define LIGHT_ICE

module Lights
{
  sequence<int> list;

  exception NoInput {};
  exception OutOfBoundry{};

  interface Light
  {
    void switch(int id) throws OutOfBoundry ;
    int print(int id) throws OutOfBoundry ;
    list all();
  };

};

#endif