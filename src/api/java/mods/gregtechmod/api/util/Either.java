package mods.gregtechmod.api.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Either<A, B> {
    @Nullable
    private final A left;
    @Nullable
    private final B right;
    
    public static <A, B> Either<A, B> left(@Nonnull A left) {
        return new Either<>(left, null);
    }
    
    public static <A, B> Either<A, B> right(@Nonnull B right) {
        return new Either<>(null, right);
    }
    
    private Either(A left, B right) {
        if (left != null && right != null) {
            throw new IllegalArgumentException("Only one value may be non-null");
        }
        else if (left == null && right == null) {
            throw new IllegalArgumentException("At least one value must be non-null");
        }
        
        this.left = left;
        this.right = right;
    }
    
    public void when(Consumer<A> left, Consumer<B> right) {
        if (isLeft()) left.accept(this.left);
        else right.accept(this.right);
    }
    
    public <T> T mapWhen(Function<A, T> left, Function<B, T> right) {
        return isLeft() ? left.apply(this.left) : right.apply(this.right);
    }
    
    public boolean isLeft() {
        return this.left != null;
    }
    
    public boolean isRight() {
        return this.right != null;
    }
    
    @Nullable
    public A getLeft() {
        return this.left;
    }
    
    @Nullable
    public B getRight() {
        return this.right;
    }
}
