# Dagger

Dagger:
-------

An extension to the kotlin compiler.
A Dependency injection framework.

Dependency Injection:
---------------------

Dependency injection separates the creation of a client's dependencies from the client's behavior, which allows program designs to be loosely coupled and to follow the dependency inversion and single responsibility principles. 

Inversion of Control:
---------------------

IoC is all about inverting the control. Inversion of Control which says that a class should get its dependencies from outside. In simple words, no class should instantiate another class but should get the instances from a configuration class.


To explain this in layman's terms, suppose you drive a car to your work place. This means you control the car. The IoC principle suggests to invert the control, meaning that instead of driving the car yourself, you hire a cab, where another person will drive the car. Thus, this is called inversion of the control - from you to the cab driver. You don't have to drive a car yourself and you can let the driver do the driving so that you can focus on your main work.

The IoC principle helps in designing loosely coupled classes which make them testable, maintainable and extensible.

Limitations of Dagger:
----------------------

Dagger does not inject fields automatically.
It cannot inject private fields.
If you want to use field injection you have to define a method in your @Component annotated interface which takes the instance of the class into which you want to inject the member variable.

Disadvantages:
--------------

1) The documentation is pretty horrible.
2) Dagger can do some very complex stuff with very little code; it can be pretty hard for the next person to read.
3) Dagger tries to infer your intention from a few annotations; when it misunderstands you, it can be difficult to figure out why
4) Dagger is lazy; you can have incorrect code that only explodes when its actually used
5) Dagger generates code; when there are errors in the generated code, it can be difficult to figure out what to do

The Dependency Graph:
---------------------

1) It must know to create objects of each of the required types(classes), and what their dependencies are : @Inject

2) It must have a top-level factory, from which application code can get the objects it needs: @Component

Dagger in a Nutshell:
---------------------

1) Dagger allows us to declaratively specify how an object of a given type is created and what its dependencies are, in a single annotation: @Inject

2) Dagger implements an abstract interface annotated with the @Component annotation; Dagger will create the graph of factories necessary to supply any object returned by a component method

Method Injection:
-----------------

class ViewModel {
	@Inject constructor()
	
	var client: Client? =null
		@Inject set
		
	fun connect(show: (String?) -> Unit) {
		val data = client?.fetchData()
		show(data)
	}
}	

	
Field Injection:
----------------

@Inject
lateinit var presenter: Presenter

Provider Injection:
-------------------

A Provider<T> is a factory for objects of type T. If you code depends on multiple objects of type T, inject the Provider<T> and use it's get method to get the instances

Lazy Injection:
---------------

A Lazy<T> is a factory for objects of type T. If your code needs a lazily created singleton of type T, inject the Lazy<T> and use it's get method to get the instances

@Modules:
--------

1) Modules provide Dagger with additional information that it cannot infer for itself

2) Modules are created by annotating an interface or an object with @Module annotation

3) Modules must be included in a component using a modules argument: @Component(modules = [MyModule::class])

4) Modules shows how to use interface binding in dagger or how to use sub class in place of super class

@Module
interface ConnectionModule {
	@Binds
	fun bindConnection(network : Network) : Connection
}

@Provides:
----------

1) Sometime Dagger cannot infer instances, for example a third party libraries. We may have a scenario that we can't inject instances of the third party class through @Inject.

2) To overcome this Dagger provides @Provides, which provides the instances of the class.

@Module
interface ConnectionModule {
	@Provides
	@JvmStatic
	fun connection() = Connection.get()
} 

@Named:
-------

1) Use @Named to distinguish among different objects with the same type

2) Annotate the object: @Provides or @Inject annotation

3) Use @Named annotation with the corresponding name parameter to identify a particular object

@Module
object ConnectionModule {
	@Provides
	@JvmStatic
	@Named("endpoint")
	fun providesEndpoint() = "Url"
	
	@Provides
	@JvmStatic
	@Named("startpoint")
	fun providesEndpoint() = "Url"
}

class Connection @Inject constructor(@Named("endpoint") val endpoint: String) {
	fun doReq() = endpoint
}

MultiBinding:
-------------

Dagger provides multiBinding for Set and Map Collection types

@Module
object ConnectionModule {
	@Provides
	@JvmStatic
	@IntoSet
	fun providesOneDecorator() = Decorator("One")
	
	@Provides
	@JvmStatic
	@ElementsIntoSet
	fun providesAnotherDecorator() = setOf(Decorator("One"), Decorator("two"))
}

@Module
object ConnectionModule {
	@Provides
	@JvmStatic
	@IntoMap
	@StringKey("one")
	fun providesOneDecorator() = Decorator("One")
}

An Abstract Factory:
--------------------

@Component annotated interface is an abstract factory for which Dagger supplies an implementation

Dagger's Implementation:
------------------------

1) Dagger's implementation of @Component annotated interface will be named Dagger<interface-name>

2) Dagger's implementation of @Component is in the directory $PROJECT_ROOT/build/generated/source/kapt/<flavor>

Getting Dagger's Factory:
-------------------------

1) So far, we've used the static create() method, to get Dagger's implementation of the abstract factory

2) Dagger uses the builder pattern to initialize new @Component instances: create() is an abbreviation of DaggerFactory.builder().build()

Explicitly Specifying Modules:
------------------------------

you'll have to explicitly supply a module to dagger when:

1) The module is concrete(not an interface)

2) The module has methods that are not static

3) The module's constructor takes arguments whose type are unknown to Dagger

Extending the Component Builder:
--------------------------------

1) Create a new interface, inside the component

2) Annotate the interface with @Component.Builder

3) The interface must declare a method build() that takes no arguments and returns the component type

4) Add builder methods

Creating Builder Methods:
-------------------------

1) Each method must take a single parameter: the object being provided to the @Component

2) Each method must return the builder interface type

3) Each argument method must be annotated with the @Bindsinstance annotation

Component Dependencies:
-----------------------

1) Declare component dependencies in the dependencies parameters to the @Component annotation

2) Dagger will not automatically create needed components

3) Pass a component dependencies to it with its builder's <component-name>(...) method

4) Component dependencies must explicitly provide the types to their super components need
 

@Component
interface NetworkModule {
	@Provides
	@JvmStatic
	fun connection() = Connection.get()
} 

@Component(dependencies= [NetworkModule::class])
interface ConnectionModule {

} 

SubComponents:
--------------

1) Create a component exactly as you would any other component

2) Replace the @Component annotation with @Subcomponent annotation

Creating SubComponents:
-----------------------

1) Add a new method to the parent component

2) New method returns the subcomponent type

3) The new method must take as arguments instances of any required modules whose constructors have arguments

4) Add the builder interface to the subcomponent; the rules are the same as they were for components except that the annotation is @SubComponent.Builder

5) The parent component's factory method mus return the subcomponent's builder, not the subcomponent

6) Use the parent component's factory method to get the subcomponent builder, add parameters, and build

Two types of scope in Dagger:
-----------------------------

Singleton: 
----------

1) return single instance of the object.

2) Lazy<T> is a singleton

3) Singletons that you @Provide to Dagger are singletons

4) @Singletons returned by two different @Components are not identical

5) Normal Java rules apply: an object is reachable from the time it is created until there are no more reachable references to it

@Singleton
class SingletonThing @Inject constructor(...)

Reusable: 
---------

1) It doesn't guarantee single instance. But most of time it creates single instance. It optimizes the  

@Reusable
class ReusableThing @Inject constructor(...)

Scoping Rules:
--------------

1) Without scope a factory creates a new instances for each request(except Lazy)

2) Scope is an attribute of a creator: @Binds, @Provides, @Inject constructors are scoped

3) @Component must be annotated with all of the scopes of all of the creators it uses

4) @Component dependencies may not cause a scope cycle

No Cycles:
----------

1) All sub or dependent components must be scoped 

2) No sub or dependent components may have the same scope (example: may be @Singleton)

Custom Scope:
-------------

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

1) Create a custom scope with @Qualifier annotated annotation class

2) Dagger treats custom @Scope exactly as if it were a singleton

3) Allow strictly hierarchical scoping in the DAG

Beware:
-------

1) Dagger cannot prevent you from holding references to multiple components with the same scope, even if that is not your intent

2) Dagger cannot prevent you from holding a reference to a component whose parent is stale

3) Singletons are only singletons in the context of their components; two components will produce singletons that are not ==


Different between @Component / @Subcomponent:
---------------------------------------------

Component dependencies - Use this when you want to keep two components independent.

Subcomponents - Use this when you want to keep two components coupled.

I will use the below example to explain Component dependencies and Subcomponents. Some points worth noticing about the example are:

SomeClassA1 can be created without any dependency. ModuleA provides and instance of SomeClassA1 via the provideSomeClassA1() method.
SomeClassB1 cannot be created without SomeClassA1. ModuleB can provide an instance of SomeClassB1 only if an instance of SomeClassA1 is passed as an argument to provideSomeClassB1() method.
@Module
public class ModuleA {
    @Provides
    public SomeClassA1 provideSomeClassA1() {
        return new SomeClassA1();
    }
}

@Module
public class ModuleB {
    @Provides
    public SomeClassB1 provideSomeClassB1(SomeClassA1 someClassA1) {
        return new SomeClassB1(someClassA1);
    }
}

public class SomeClassA1 {
    public SomeClassA1() {}
}

public class SomeClassB1 {
    private SomeClassA1 someClassA1;

    public SomeClassB1(SomeClassA1 someClassA1) {
        this.someClassA1 = someClassA1;
    }
}
Dagger will take care of passing the instance of SomeClassA1 as an argument to provideSomeClassB1() method on ModuleB whenever the Component/Subcomponent declaring ModuleB is initialized. We need to instruct Dagger how to fulfill the dependency. This can be done either by using Component dependency or Subcomponent.

Component dependency:
---------------------

Note the following points in the Component dependency example below:

ComponentB has to define the dependency via the dependencies method on @Component annotation.
ComponentA doesn't need to declare ModuleB. This keeps the two components independent.
public class ComponentDependency {
    @Component(modules = ModuleA.class)
    public interface ComponentA {
        SomeClassA1 someClassA1();
    }

    @Component(modules = ModuleB.class, dependencies = ComponentA.class)
    public interface ComponentB {
        SomeClassB1 someClassB1();
    }

    public static void main(String[] args) {
        ModuleA moduleA = new ModuleA();
        ComponentA componentA = DaggerComponentDependency_ComponentA.builder()
                .moduleA(moduleA)
                .build();

        ModuleB moduleB = new ModuleB();
        ComponentB componentB = DaggerComponentDependency_ComponentB.builder()
                .moduleB(moduleB)
                .componentA(componentA)
                .build();
    }
}
SubComponent:
-------------

Note the following points in the SubComponent example:

As ComponentB has not defined the dependency on ModuleA, it cannot live independently. It becomes dependent on the component that will provide the ModuleA. Hence it has a @Subcomponent annotation.
ComponentA has declared ModuleB via the interface method componentB(). This makes the two components coupled. In fact, ComponentB can only be initialized via ComponentA.
public class SubComponent {
    @Component(modules = ModuleA.class)
    public interface ComponentA {
        ComponentB componentB(ModuleB moduleB);
    }

    @Subcomponent(modules = ModuleB.class)
    public interface ComponentB {
        SomeClassB1 someClassB1();
    }

    public static void main(String[] args) {
        ModuleA moduleA = new ModuleA();
        ComponentA componentA = DaggerSubComponent_ComponentA.builder()
                .moduleA(moduleA)
                .build();

        ModuleB moduleB = new ModuleB();
        ComponentB componentB = componentA.componentB(moduleB);
    }
}

Different between Builder / Create:
-----------------------------------

create() vs builder().build()
Instead of using create(), if you use builder() you can pass arguments to any of the module constructors (if they take any params). This is a way to pass information that is not available in the dependency graph that Dagger 2 generates. In this case, each module has a default constructor, so there’s really no need to do it this way, and create() would suffice.

DaggerPingComponent.builder()
            .networkModule(NetworkModule(this))
            .build()
            .inject(this)
			
Different between @Provider / @Lazy:
------------------------------------

Provider Injection:
-------------------

A Provider<T> is a factory for objects of type T. If you code depends on multiple objects of type T, inject the Provider<T> and use it's get method to get the instances.
It injects new instances every time when we calling get method.

Lazy Injection:
---------------

A Lazy<T> is a factory for objects of type T. If your code needs a lazily created singleton of type T, inject the Lazy<T> and use it's get method to get the instances.
It injects single instances through out the application.

Different between @Singleton / @Reusable / @Custom Scope:
---------------------------------------------------------

Use @Singleton if you rely on singleton behavior and guarantees. Use @Reusable if an object would only be a @Singleton for performance reasons.

@Reusable bindings have much more in common with unscoped bindings than @Singleton bindings: You're telling Dagger that you'd be fine creating a brand-new object, but if there's a convenient object already created then Dagger may use that one. In contrast, @Singleton objects guarantee that you will always receive the same instance, which can be much more expensive to enforce.

Different between @IntoMap / @IntoSet:
--------------------------------------

MultiBinding can be achieved by using @IntoMap and @IntoSet.

@Module
object ConnectionModule {
	@Provides
	@JvmStatic
	@IntoSet
	fun providesOneDecorator() = Decorator("One")
	
	@Provides
	@JvmStatic
	@ElementsIntoSet
	fun providesAnotherDecorator() = setOf(Decorator("One"), Decorator("two"))
}

@Module
object ConnectionModule {
	@Provides
	@JvmStatic
	@IntoMap
	@StringKey("one")
	fun providesOneDecorator() = Decorator("One")
}

Different between Constructor injection / Field injection / Method injection:
-----------------------------------------------------------------------------

Constructor Injection:
----------------------
Injecting the method parameters.

class MainViewModel @Inject constructor(private val repository: PingRepository)

Field Injection: 
----------------
Injecting the member variable (must not be private).

@Inject
lateinit var presenter: Presenter

Method Injection: 
-----------------
Injecting the method parameter.

class ViewModel {
	@Inject constructor()
	
	var client: Client? =null
		@Inject set
		
	fun connect(show: (String?) -> Unit) {
		val data = client?.fetchData()
		show(data)
	}
}	


Dependency Provider/Consumer:
-----------------------------

A dependency consumer asks for the dependency(Object) from a dependency provider through a connector.

Dependency provider: Classes annotated with @Module are responsible for providing objects which can be injected. Such classes define methods annotated with @Provides. The returned objects from these methods are available for dependency injection.

Dependency consumer: The @Inject annotation is used to define a dependency.

Connecting consumer and producer: A @Component annotated interface defines the connection between the provider of objects (modules) and the objects which express a dependency. The class for this connection is generated by the Dagger.

@Named Annotation:
------------------

1) Use @Named to distinguish among different objects with the same type

2) Annotate the object: @Provides or @Inject annotation

3) Use @Named annotation with the corresponding name parameter to identify a particular object

@Module
object ConnectionModule {
	@Provides
	@JvmStatic
	@Named("endpoint")
	fun providesEndpoint() = "Url"
	
	@Provides
	@JvmStatic
	@Named("startpoint")
	fun providesEndpoint() = "Url"
}

class Connection @Inject constructor(@Named("endpoint") val endpoint: String) {
	fun doReq() = endpoint
}

@Modules Annotation:
--------------------

1) Modules provide Dagger with additional information that it cannot infer for itself

2) Modules are created by annotating an interface or an object with @Module annotation

3) Modules must be included in a component using a modules argument: @Component(modules = [MyModule::class])

4) Modules shows how to use interface binding in dagger or how to use sub class in place of super class

@Module
interface ConnectionModule {
	@Binds
	fun bindConnection(network : Network) : Connection
}

@Provides:
----------

1) Sometime Dagger cannot infer instances, for example a third party libraries. We may have a scenario that we can't inject instances of the third party class through @Inject.

2) To overcome this Dagger provides @Provides, which provides the instances of the class.

@Module
interface ConnectionModule {
	@Provides
	@JvmStatic
	fun connection() = Connection.get()
} 

@Inject Annotation:
-------------------
@Inject provides dependency injection at runtime.
Use @Inject to annotate the constructor that Dagger should use to create instances of a class. When a new instance is requested, Dagger will obtain the required parameters values and invoke this constructor.
@Inject instructs dagger to create an instance.

class MainViewModel @Inject constructor(private val repository: PingRepository)

Different between @Provides / @Binds:
-------------------------------------

@Binds can be perfectly equivalent to a @Provides-annotated method like this:

@Provides
public HomePresenter provideHomePresenter() {
    return new HomePresenterImp();
}
...though you'd probably prefer a variant that takes HomePresenterImp as a method parameter, which lets Dagger instantiate HomePresenterImp (assuming it has an @Inject constructor) including passing any dependencies it needs. You can also make this static, so Dagger doesn't need to instantiate your Module instance to call it.

@Provides
public static HomePresenter provideHomePresenter(HomePresenterImp presenter) {
    return presenter;
}
So why would you choose @Binds instead? Dagger has a FAQ about it, but it boils down do these reasons:

1) @Binds is (slightly) more compact: You can skip the implementation.
2) @Binds works in interfaces and abstract classes, which are strictly required for Dagger features like @BindsOptionalOf and @ContributesAndroidInjector.
3) @Binds helps your code stay efficient. @Provides methods can be instance methods, which require Dagger to instantiate your Module in order to call them. Making your @Provides method static will also accomplish this, but your @Provides method will still compile if you forget the static. @Binds methods will not.
4) @Binds prevents Dagger from having to codegen and keep a separate Factory/Provider for the object, since Java doesn't give Dagger access to know that the implementation is as simple as it is. In your case, Dagger can cast the Provider<HomePresenterImp> to a Provider<HomePresenter> and only keep one, rather than keeping one for HomePresenter that does nothing but call the one for HomePresenterImp.
Thus, the entire thing would be well-represented as:

@Binds abstract HomePresenter bindHomePresenter(HomePresenterImp presenter);

@BindsInstance:
---------------

Let’s take a very simple example. We have an AppComponent defined as follows :

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
  
  void inject(MainActivity mainActivity);
  SharedPreferences getSharedPrefs();
}

Here’s our simple AppModule :

 @Module
 public class AppModule {
 
    Application application;
 
    public AppModule(Application application) {
       this.application = application;
    }
 
    @Provides
    Application providesApplication() {
       return application;
    }
    @Provides
    @Singleton
    public SharedPreferences providePreferences() {
        return application.getSharedPreferences(DATA_STORE,
                              Context.MODE_PRIVATE);
    }
 
 }

Instantiating a Component
Components can be instantiated by using the Builders generated by Dagger :
DaggerAppComponent appComponent = DaggerAppComponent.builder()
         .appModule(new AppModule(this)) //this : application 
         .build();
Dagger allows us to customize the generated builder by something knows as a Component.Builder

@Component.Builder: 
-------------------

Firstly, Let’s refer to the documentation of @Component.Builder
A builder for a component. Components may have a single nested static abstract class or interface annotated with @Component.Builder. If they do, then the component's generated builder will match the API in the type.

`@BindsInstance What?`
Here’s the definition :
Marks a method on a component builder or subcomponent builder that allows an instance to be bound to some type within the component. 
Here’s a simple hint of when to use it :
@BindsInstance methods should be preferred to writing a @Module with constructor arguments and immediately providing those values. 

Let’s look at our simplified AppModule with the constructor and @Provides Application removed :

@Module
public class AppModule {

    @Provides
    @Singleton
    public SharedPreferences providePreferences(
                                   Application application) {
        return application.getSharedPreferences(
                                   "store", Context.MODE_PRIVATE);
    }
}

Here’s our customised Component.Builder now :

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
   void inject(MainActivity mainActivity);
   SharedPreferences getSharedPrefs();
   @Component.Builder
   interface Builder {
   
      AppComponent build();
      @BindsInstance Builder application(Application application);      
  }
}

Note that we need not specify Builder appModule(AppModule appModule); inside the Component.Builder anymore as we are going to let dagger use the default constructor of AppModule now.

Here’s how we will initialize our DaggerAppComponent :
DaggerAppComponent appComponent = DaggerAppComponent.builder()
           .application(this)
           .build();
