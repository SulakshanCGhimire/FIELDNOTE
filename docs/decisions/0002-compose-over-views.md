# ADR 0002: Jetpack Compose over XML Views

## Context
FIELDNOTE needs a UI that can grow beyond a few simple screens. The application will have things such as report lists, report details, report editing, search and filtering, and other interactive UI.

The main choice is whether to build the UI using the traditional XML-based Android Views system or Jetpack Compose.

## Options

### Option 1: XML Views
- **Advantage:** XML Views are a mature Android UI system with a large amount of existing documentation and code.
- **Disadvantage:** UI layout and UI logic are split between XML and Kotlin, which can make some UI changes more verbose and harder to reason about.

### Option 2: Jetpack Compose
- **Advantage:** UI can be written directly in Kotlin using a declarative approach, making it easier to describe how the UI should look based on its current state.
- **Disadvantage:** It is a different way of thinking about Android UI, so there is a learning curve, especially when coming from the traditional Views approach.

## Decision
We will use Jetpack Compose for FIELDNOTE's primary UI.

The main reason is that FIELDNOTE will have several interactive and state-driven screens, and Compose's declarative approach fits well with the kind of state-based UI we need. This also gives me an opportunity to learn the modern Android UI approach rather than building the project primarily around the older XML Views model.

Compose and XML Views can coexist in the same Android application, so this decision does not prevent us from using existing Views or integrating a View-based component when needed. This reduces the risk of being locked into one UI approach.

## Consequences
- We gain a declarative, state-driven approach to building interactive screens, but must learn Compose's mental model and state-management practices.
- We give up the convenience of directly following some older XML-based tutorials and reusing certain View-based UI implementations without adaptation.
- We should prefer Compose for new screens while introducing Views only when there is a clear reason to do so.