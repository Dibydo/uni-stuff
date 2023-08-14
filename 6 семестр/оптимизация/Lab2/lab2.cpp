#include "llvm/ADT/APInt.h"
#include "llvm/IR/Verifier.h"
#include "llvm/ExecutionEngine/ExecutionEngine.h"
#include "llvm/ExecutionEngine/GenericValue.h"
#include "llvm/ExecutionEngine/MCJIT.h"
#include "llvm/IR/Argument.h"
#include "llvm/IR/BasicBlock.h"
#include "llvm/IR/Constants.h"
#include "llvm/IR/DerivedTypes.h"
#include "llvm/IR/Function.h"
#include "llvm/IR/InstrTypes.h"
#include "llvm/IR/Instructions.h"
#include "llvm/IR/LLVMContext.h"
#include "llvm/IR/Module.h"
#include "llvm/IR/Type.h"
#include "llvm/Support/Casting.h"
#include "llvm/Support/TargetSelect.h"
#include "llvm/Support/raw_ostream.h"

#include "llvm/IR/IRBuilder.h"

#include <algorithm>
#include <cstdlib>
#include <memory>
#include <string>
#include <vector>

// int main() {
//      return 353 + 48;
// }

using namespace llvm;

int main(int argc, char **argv) {
    InitializeNativeTarget();
    InitializeNativeTargetAsmPrinter();

    LLVMContext context;
    Module *module = new Module("top", context);

    IRBuilder<> builder(context);
    FunctionType *funcType = FunctionType::get(builder.getInt32Ty(), false);
    Function *mainFunc = Function::Create(funcType, Function::ExternalLinkage, "main", module);

    BasicBlock *entry = BasicBlock::Create(context, "entrypoint", mainFunc);
    builder.SetInsertPoint(entry);

    Value *const1 = ConstantInt::get(Type::getInt32Ty(context), 353);
    Value *const2 = ConstantInt::get(Type::getInt32Ty(context), 48);
    Value *retval = builder.CreateAdd(const1, const2, "ret");
    builder.CreateRet(retval); 

    module->print(outs(), nullptr);

    delete module;
    return 0;
}
